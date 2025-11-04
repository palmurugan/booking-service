package com.serviq.booking.strategy.payment;

import com.serviq.booking.dto.response.PaymentInitiationResponse;
import com.serviq.booking.entity.Booking;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Strategy interface for different payment processing methods
 */
public interface PaymentStrategy {

    PaymentInitiationResponse processPayment(UUID bookingId, BigDecimal amount);

    Booking.BookingStatus getBookingStatus();

    boolean requiresImmediateConfirmation();
}
