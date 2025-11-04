package com.serviq.booking.dto.response;

import com.serviq.booking.entity.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private UUID bookingId;
    private UUID orgId;
    private UUID userId;
    private ServiceInfo serviceInfo;
    private ProviderInfo providerInfo;
    private SlotInfo slotInfo;
    private Booking.BookingStatus status;
    private BigDecimal totalAmount;
    private UUID paymentId;
    private String specialInstructions;
    private String cancellationReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServiceInfo {
        private UUID serviceId;
        private String serviceName;
        private String category;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProviderInfo {
        private UUID providerId;
        private String providerName;
        private String contactNumber;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SlotInfo {
        private UUID slotId;
        private LocalDate bookingDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
