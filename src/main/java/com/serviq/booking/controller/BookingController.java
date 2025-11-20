package com.serviq.booking.controller;

import com.serviq.booking.dto.request.CancelBookingRequest;
import com.serviq.booking.dto.request.ConfirmBookingRequest;
import com.serviq.booking.dto.request.CreateBookingRequest;
import com.serviq.booking.dto.response.BookingResponse;
import com.serviq.booking.dto.response.PaymentInitiationResponse;
import com.serviq.booking.entity.Booking;
import com.serviq.booking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {
        log.info("POST /api/v1/bookings - Session: {}, Payment Method: {}",
                request.getSessionId(), request.getPaymentMethod());
        BookingResponse response = bookingService.createBooking(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<BookingResponse> confirmBooking(
            @Valid @RequestBody ConfirmBookingRequest request) {
        log.info("POST /api/v1/bookings/confirm - Booking: {}, Transaction: {}",
                request.getBookingId(), request.getTransactionId());
        BookingResponse response = bookingService.confirmBooking(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponse> getBooking(
            @PathVariable UUID bookingId) {
        log.info("GET /api/v1/bookings/{}", bookingId);
        BookingResponse response = bookingService.getBooking(bookingId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Page<BookingResponse>> getUserBookings(
            @PathVariable UUID userId,
            @RequestParam(required = false) Booking.BookingStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("GET /api/v1/bookings/user/{} - Status: {}, Page: {}, Size: {}",
                userId, status, page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<BookingResponse> response = bookingService.getUserBookings(userId, status, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(
            @Valid @RequestBody CancelBookingRequest request) {
        log.info("POST /api/v1/bookings/cancel - Booking: {}, Reason: {}",
                request.getBookingId(), request.getReason());
        BookingResponse response = bookingService.cancelBooking(request);
        return ResponseEntity.ok(response);
    }
}
