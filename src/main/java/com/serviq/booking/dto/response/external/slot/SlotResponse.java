package com.serviq.booking.dto.response.external.slot;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SlotResponse {

    public enum SlotStatus {
        AVAILABLE,
        BOOKED,
        BLOCKED,
        CANCELLED
    }

    private UUID id;
    private UUID orgId;
    private UUID providerId;
    private UUID providerServiceId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate slotDate;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    private Integer durationMinutes;
    private Integer capacity;
    private Integer bookedCount;
    private SlotStatus status;
    private Boolean isAvailable;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
