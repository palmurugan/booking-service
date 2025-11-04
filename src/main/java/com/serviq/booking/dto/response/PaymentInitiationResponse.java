package com.serviq.booking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitiationResponse {

    private UUID paymentId;
    private UUID bookingId;
    private String orderId;
    private String paymentGatewayUrl;
    private String checksum;
    private PaymentDetails paymentDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentDetails {
        private String amount;
        private String currency;
        private String merchantId;
        private String paymentMethod;
        private String callbackUrl;
    }
}
