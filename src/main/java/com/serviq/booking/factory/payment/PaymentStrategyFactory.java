package com.serviq.booking.factory.payment;

import com.serviq.booking.dto.request.CreateBookingRequest;
import com.serviq.booking.exception.BusinessException;
import com.serviq.booking.strategy.payment.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class PaymentStrategyFactory {

    private final Map<String, PaymentStrategy> strategies;

    public PaymentStrategyFactory(Map<String, PaymentStrategy> strategies) {
        this.strategies = strategies;
        log.info("Payment strategies loaded: {}", strategies.keySet());
    }

    public PaymentStrategy getPaymentStrategy(CreateBookingRequest.PaymentMethod paymentMethod) {
        PaymentStrategy strategy = strategies.get(paymentMethod.name());

        if (strategy == null) {
            throw new BusinessException("Unsupported payment method: " + paymentMethod);
        }
        return strategy;
    }
}
