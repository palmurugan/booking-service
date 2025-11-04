package com.serviq.booking.controller;

import com.serviq.booking.dto.request.ApplyCouponRequest;
import com.serviq.booking.dto.request.CheckoutInitiateRequest;
import com.serviq.booking.dto.response.CheckoutSessionResponse;
import com.serviq.booking.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/initiate")
    public ResponseEntity<CheckoutSessionResponse> initiateCheckout(
            @Valid @RequestBody CheckoutInitiateRequest request) {
        log.info("POST /api/v1/checkout/initiate - User: {}, Service: {}",
                request.getUserId(), request.getServiceId());

        CheckoutSessionResponse response = checkoutService.initiateCheckout(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/coupon/apply")
    public ResponseEntity<CheckoutSessionResponse> applyCoupon(
            @Valid @RequestBody ApplyCouponRequest request) {
        log.info("POST /api/v1/checkout/coupon/apply - Session: {}, Coupon: {}",
                request.getSessionId(), request.getCouponCode());

        CheckoutSessionResponse response = checkoutService.applyCoupon(
                request.getSessionId(),
                request.getCouponCode()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<CheckoutSessionResponse> getCheckoutSession(
            @PathVariable UUID sessionId) {
        log.info("GET /api/v1/checkout/{}", sessionId);
        CheckoutSessionResponse response = checkoutService.getCheckoutSession(sessionId);
        return ResponseEntity.ok(response);
    }
}
