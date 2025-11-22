package com.serviq.booking.helper;

import com.serviq.booking.client.SlotServiceClient;
import com.serviq.booking.dto.response.external.slot.SlotResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckoutHelper {

    private final SlotServiceClient slotServiceClient;

    public SlotResponse blockSlot(UUID slotId) {
        log.info("Service Call: Block the slot {}", slotId);
        return slotServiceClient.updateSlotStatus(slotId, SlotResponse.SlotStatus.BLOCKED);
    }
}
