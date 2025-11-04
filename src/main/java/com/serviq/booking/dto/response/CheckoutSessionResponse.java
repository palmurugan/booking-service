package com.serviq.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutSessionResponse {

    private UUID sessionId;
    private UUID orgId;
    private UUID userId;
    private UUID serviceId;
    private UUID providerId;
    private UUID slotId;
    private List<AddonDetail> selectedAddons;
    private String appliedCoupon;
    private PriceBreakdown priceBreakdown;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private boolean isExpired;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AddonDetail {
        private UUID addonId;
        private String name;
        private BigDecimal price;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PriceBreakdown {
        private BigDecimal subtotal;
        private BigDecimal discount;
        private BigDecimal totalAmount;
    }
}
