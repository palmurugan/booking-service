package com.serviq.booking.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutInitiateRequest {

    @NotNull(message = "Organization ID is required")
    private UUID orgId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @NotNull(message = "Service ID is required")
    private UUID serviceId;

    @NotNull(message = "Provider ID is required")
    private UUID providerId;

    @NotNull(message = "Slot ID is required")
    private UUID slotId;

    private List<UUID> addonIds;

    @Size(max = 500, message = "Special instructions cannot exceed 500 characters")
    private String specialInstructions;
}
