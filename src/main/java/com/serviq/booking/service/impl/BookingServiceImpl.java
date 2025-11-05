package com.serviq.booking.service.impl;

import com.serviq.booking.dto.request.CancelBookingRequest;
import com.serviq.booking.dto.request.ConfirmBookingRequest;
import com.serviq.booking.dto.request.CreateBookingRequest;
import com.serviq.booking.dto.response.BookingResponse;
import com.serviq.booking.dto.response.PaymentInitiationResponse;
import com.serviq.booking.entity.Booking;
import com.serviq.booking.entity.CheckoutSession;
import com.serviq.booking.exception.BookingException;
import com.serviq.booking.exception.CheckoutExpiredException;
import com.serviq.booking.exception.ResourceNotFoundException;
import com.serviq.booking.factory.payment.PaymentStrategyFactory;
import com.serviq.booking.repository.BookingRepository;
import com.serviq.booking.repository.CheckoutSessionRepository;
import com.serviq.booking.service.BookingService;
import com.serviq.booking.strategy.payment.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CheckoutSessionRepository checkoutSessionRepository;
    private final PaymentStrategyFactory paymentStrategyFactory;


    @Override
    @Transactional
    public PaymentInitiationResponse createBooking(CreateBookingRequest request) {
        log.info("Creating booking for session: {}", request.getSessionId());

        // Get and validate checkout session
        CheckoutSession session = checkoutSessionRepository.findByIdAndIsActiveTrue(request.getSessionId())
                .orElseThrow(() -> ResourceNotFoundException.checkoutSession(request.getSessionId()));

        if (session.isExpired()) {
            throw CheckoutExpiredException.session();
        }

        // Validate slot still available
        if (bookingRepository.existsBySlotIdAndStatusInAndIsActiveTrue(
                session.getSlotId(),
                Arrays.asList(Booking.BookingStatus.CONFIRMED, Booking.BookingStatus.PENDING))) {
            throw BookingException.invalidStatus("BOOKED", "AVAILABLE");
        }

        // TODO call provider service to get the slot details.

        // Getting the correct payment strategy
        PaymentStrategy paymentStrategy = paymentStrategyFactory.getPaymentStrategy(request.getPaymentMethod());

        // Create booking
        Booking booking = Booking.builder()
                .orgId(session.getOrgId())
                .userId(session.getUserId())
                .serviceId(session.getServiceId())
                .providerId(session.getProviderId())
                .slotId(session.getSlotId())
                .bookingDate(LocalDate.now())
                // TODO update the slot times
                .startTime(LocalTime.now())
                .endTime(LocalTime.now().plusHours(1))

                .status(paymentStrategy.getBookingStatus())
                .totalAmount(session.getTotalAmount())
                .isActive(true)
                .build();

        booking = bookingRepository.save(booking);
        log.info("Booking created with ID: {}", booking.getId());

        // TODO payment integration may come here - revisit the architecture
        // Process payment using strategy
        PaymentInitiationResponse paymentResponse = paymentStrategy.processPayment(
                booking.getId(),
                session.getTotalAmount()
        );

        booking.setPaymentId(paymentResponse.getPaymentId());
        bookingRepository.save(booking);

        // Handle immediate confirmation for cash payments
        if (paymentStrategy.requiresImmediateConfirmation()) {
            confirmBookingImmediately(booking);
        }

        // Deactivate checkout session
        session.setIsActive(false);
        checkoutSessionRepository.save(session);
        return paymentResponse;
    }

    /**
     * Confirms booking immediately (used for cash on service)
     */
    private void confirmBookingImmediately(Booking booking) {
        log.info("Confirming booking immediately: {}", booking.getId());

        // Confirm the slot

        // Send confirmation notification
    }

    @Override
    @Transactional
    public BookingResponse confirmBooking(ConfirmBookingRequest request) {
        log.info("Confirming booking: {}", request.getBookingId());

        Booking booking = bookingRepository.findByIdAndIsActiveTrue(request.getBookingId())
                .orElseThrow(() -> ResourceNotFoundException.booking(request.getBookingId()));

        if (booking.getStatus() != Booking.BookingStatus.PAYMENT_INITIATED) {
            throw BookingException.invalidStatus(
                    booking.getStatus().name(),
                    Booking.BookingStatus.PAYMENT_INITIATED.name()
            );
        }

        // TODO Payment verification should be handle here.
        // TODO Slot confirmation call should happen here.

        // Update booking status
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

        log.info("Booking confirmed: {}", booking.getId());

        // Todo publish notification event.

        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBooking(UUID bookingId) {
        log.info("Fetching booking: {}", bookingId);
        Booking booking = bookingRepository.findByIdAndIsActiveTrue(bookingId)
                .orElseThrow(() -> ResourceNotFoundException.booking(bookingId));
        return mapToResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> getUserBookings(UUID userId, Booking.BookingStatus status, Pageable pageable) {
        log.info("Fetching bookings for user: {}, status: {}", userId, status);

        Page<Booking> bookings;
        if (status != null) {
            bookings = bookingRepository.findByUserIdAndStatusAndIsActiveTrueOrderByCreatedAtDesc(
                    userId, status, pageable);
        } else {
            bookings = bookingRepository.findByUserIdAndIsActiveTrueOrderByCreatedAtDesc(userId, pageable);
        }

        return bookings.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public BookingResponse cancelBooking(CancelBookingRequest request) {
        log.info("Cancelling booking: {}", request.getBookingId());

        Booking booking = bookingRepository.findByIdAndIsActiveTrue(request.getBookingId())
                .orElseThrow(() -> ResourceNotFoundException.booking(request.getBookingId()));

        // Validate booking can be cancelled
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw BookingException.alreadyCancelled();
        }

        if (booking.getStatus() != Booking.BookingStatus.CONFIRMED) {
            throw BookingException.invalidStatus(booking.getStatus().name(), "CONFIRMED");
        }

        // Check cancellation window (24 hours before appointment)
        LocalDateTime appointmentTime = booking.getBookingDate().atTime(booking.getStartTime());
        LocalDateTime cutoffTime = appointmentTime.minusHours(24);

        if (LocalDateTime.now().isAfter(cutoffTime)) {
            throw BookingException.cancellationNotAllowed();
        }

        // TODO Publish slot release event

        // Update booking
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setCancellationReason(request.getReason());
        booking = bookingRepository.save(booking);

        log.info("Booking cancelled: {}", booking.getId());

        // TODO publish notification event - cancel
        return mapToResponse(booking);
    }

    private BookingResponse mapToResponse(Booking booking) {
        // var serviceInfo = serviceInfoService.getServiceInfo(booking.getServiceId());
        // var providerInfo = serviceInfoService.getProviderInfo(booking.getProviderId());

        return BookingResponse.builder()
                .bookingId(booking.getId())
                .orgId(booking.getOrgId())
                .userId(booking.getUserId())
                //.serviceInfo(serviceInfo)
                //.providerInfo(providerInfo)
                .slotInfo(BookingResponse.SlotInfo.builder()
                        .slotId(booking.getSlotId())
                        .bookingDate(booking.getBookingDate())
                        .startTime(booking.getStartTime())
                        .endTime(booking.getEndTime())
                        .build())
                .status(booking.getStatus())
                .totalAmount(booking.getTotalAmount())
                .paymentId(booking.getPaymentId())
                .specialInstructions(booking.getSpecialInstructions())
                .cancellationReason(booking.getCancellationReason())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }
}
