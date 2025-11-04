package com.serviq.booking.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class ConfirmBookingRequest {

    @NotNull(message = "Booking ID is required")
    private UUID bookingId;

    @NotBlank(message = "Transaction ID is required")
    private String transactionId;

    private String paymentStatus;
}
