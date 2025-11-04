package com.serviq.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookingRequest {

    @NotNull(message = "Session ID is required")
    private UUID sessionId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    public enum PaymentMethod {
        CARD, UPI, WALLET, NET_BANKING, CASH_ON_SERVICE
    }
}
