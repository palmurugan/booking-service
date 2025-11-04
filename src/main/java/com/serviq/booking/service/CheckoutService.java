package com.serviq.booking.service;

import com.serviq.booking.dto.request.CheckoutInitiateRequest;
import com.serviq.booking.dto.response.CheckoutSessionResponse;

import java.util.UUID;

public interface CheckoutService {
    CheckoutSessionResponse initiateCheckout(CheckoutInitiateRequest request);
    CheckoutSessionResponse applyCoupon(UUID sessionId, String couponCode);
    CheckoutSessionResponse getCheckoutSession(UUID sessionId);
    void deactivateExpiredSessions();
}
