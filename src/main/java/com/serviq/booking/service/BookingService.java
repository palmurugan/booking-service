package com.serviq.booking.service;

import com.serviq.booking.dto.request.CancelBookingRequest;
import com.serviq.booking.dto.request.ConfirmBookingRequest;
import com.serviq.booking.dto.request.CreateBookingRequest;
import com.serviq.booking.dto.response.BookingResponse;
import com.serviq.booking.dto.response.PaymentInitiationResponse;
import com.serviq.booking.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookingService {
    PaymentInitiationResponse createBooking(CreateBookingRequest request);

    BookingResponse confirmBooking(ConfirmBookingRequest request);

    BookingResponse getBooking(UUID bookingId);

    Page<BookingResponse> getUserBookings(UUID userId, Booking.BookingStatus status, Pageable pageable);

    BookingResponse cancelBooking(CancelBookingRequest request);
}
