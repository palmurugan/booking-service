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
public class ApplyCouponRequest {

    @NotNull(message = "Session ID is required")
    private UUID sessionId;

    @NotBlank(message = "Coupon code is required")
    private String couponCode;
}
