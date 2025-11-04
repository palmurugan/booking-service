package com.serviq.booking.strategy.payment;

import com.serviq.booking.dto.response.PaymentInitiationResponse;
import com.serviq.booking.entity.Booking;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@Component("CASH_ON_SERVICE")
@RequiredArgsConstructor
public class CashOnServicePaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentInitiationResponse processPayment(UUID bookingId, BigDecimal amount) {
        log.info("Processing CASH_ON_SERVICE payment for booking: {}", bookingId);

        // No actual payment processing needed
        // Return response indicating cash payment will be collected at service time
        return PaymentInitiationResponse.builder()
                .paymentId(UUID.randomUUID())
                .bookingId(bookingId)
                .orderId("CASH_" + System.currentTimeMillis())
                .paymentGatewayUrl(null) // No gateway URL for cash
                .paymentDetails(PaymentInitiationResponse.PaymentDetails.builder()
                        .amount(amount.toString())
                        .currency("INR")
                        .paymentMethod("CASH_ON_SERVICE")
                        .build())
                .build();
    }

    @Override
    public Booking.BookingStatus getBookingStatus() {
        return Booking.BookingStatus.CONFIRMED;
    }

    @Override
    public boolean requiresImmediateConfirmation() {
        return true;
    }
}
