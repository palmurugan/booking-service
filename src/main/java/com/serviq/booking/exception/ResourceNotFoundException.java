package com.serviq.booking.exception;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException checkoutSession(UUID sessionId) {
        return new ResourceNotFoundException("Checkout session not found with ID: " + sessionId);
    }

    public static ResourceNotFoundException booking(UUID bookingId) {
        return new ResourceNotFoundException("Booking not found with ID: " + bookingId);
    }

    public static ResourceNotFoundException slot(UUID slotId) {
        return new ResourceNotFoundException("Slot not found with ID: " + slotId);
    }

    public static ResourceNotFoundException service(UUID serviceId) {
        return new ResourceNotFoundException("Service not found with ID: " + serviceId);
    }
}
