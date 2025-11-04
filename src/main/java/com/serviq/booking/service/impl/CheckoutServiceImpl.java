package com.serviq.booking.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviq.booking.dto.request.CheckoutInitiateRequest;
import com.serviq.booking.dto.response.CheckoutSessionResponse;
import com.serviq.booking.entity.CheckoutSession;
import com.serviq.booking.exception.CheckoutExpiredException;
import com.serviq.booking.exception.ResourceNotFoundException;
import com.serviq.booking.repository.CheckoutSessionRepository;
import com.serviq.booking.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutSessionRepository checkoutSessionRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public CheckoutSessionResponse initiateCheckout(CheckoutInitiateRequest request) {
        log.info("Initiating checkout for user: {}, service: {}", request.getUserId(), request.getServiceId());

        //TODO Validate slot availability

        // TODO Create temporary slot lock

        //TODO Calculate pricing

        // Serialize addons to JSON
        String addonsJson = null;
        if (request.getAddonIds() != null && !request.getAddonIds().isEmpty()) {
            try {
                addonsJson = objectMapper.writeValueAsString(request.getAddonIds());
            } catch (JsonProcessingException e) {
                log.error("Error serializing addons", e);
            }
        }

        // Create checkout session
        CheckoutSession session = CheckoutSession.builder()
                .orgId(request.getOrgId())
                .userId(request.getUserId())
                .serviceId(request.getServiceId())
                .providerId(request.getProviderId())
                .slotId(request.getSlotId())
                .selectedAddons(addonsJson)
                .subtotal(BigDecimal.ZERO)
                .discount(BigDecimal.ZERO)
                .totalAmount(BigDecimal.ZERO)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .isActive(true)
                .build();

        session = checkoutSessionRepository.save(session);
        log.info("Checkout session created: {}", session.getId());

        return mapToResponse(session, null);
    }

    @Override
    public CheckoutSessionResponse applyCoupon(UUID sessionId, String couponCode) {
        return null;
    }

    @Override
    public CheckoutSessionResponse getCheckoutSession(UUID sessionId) {
        log.info("Fetching checkout session: {}", sessionId);
        CheckoutSession session = getActiveSession(sessionId);

        if (session.isExpired()) {
            throw CheckoutExpiredException.session();
        }

        return mapToResponse(session, null);
    }

    @Override
    @Transactional
    public void deactivateExpiredSessions() {
        int count = checkoutSessionRepository.deactivateExpiredSessions(LocalDateTime.now());
        log.info("Deactivated {} expired checkout sessions", count);
    }

    private CheckoutSession getActiveSession(UUID sessionId) {
        return checkoutSessionRepository.findByIdAndIsActiveTrue(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Checkout session not found for ID: " + sessionId));
    }

    private CheckoutSessionResponse mapToResponse(CheckoutSession session,
                                                  List<CheckoutSessionResponse.AddonDetail> addonDetails) {
        return CheckoutSessionResponse.builder()
                .sessionId(session.getId())
                .orgId(session.getOrgId())
                .userId(session.getUserId())
                .serviceId(session.getServiceId())
                .providerId(session.getProviderId())
                .slotId(session.getSlotId())
                .selectedAddons(addonDetails)
                .appliedCoupon(session.getAppliedCoupon())
                .priceBreakdown(CheckoutSessionResponse.PriceBreakdown.builder()
                        .subtotal(session.getSubtotal())
                        .discount(session.getDiscount())
                        .totalAmount(session.getTotalAmount())
                        .build())
                .expiresAt(session.getExpiresAt())
                .createdAt(session.getCreatedAt())
                .isExpired(session.isExpired())
                .build();
    }
}
