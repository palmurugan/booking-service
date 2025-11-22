package com.serviq.booking.client;

import com.serviq.booking.dto.response.external.slot.SlotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "slot-service", url = "http://localhost:7073")
public interface SlotServiceClient {

    @PatchMapping(value = "/api/v1/slots/{slotId}/status")
    SlotResponse updateSlotStatus(
            @PathVariable("slotId") UUID slotId,
            @RequestParam("status") SlotResponse.SlotStatus status);
}
