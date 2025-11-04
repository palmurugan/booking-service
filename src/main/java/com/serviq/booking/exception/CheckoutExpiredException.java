package com.serviq.booking.exception;

public class CheckoutExpiredException extends RuntimeException {

    public CheckoutExpiredException(String message) {
        super(message);
    }

    public CheckoutExpiredException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CheckoutExpiredException session() {
        return new CheckoutExpiredException("Checkout session has expired. Please start a new checkout.");
    }
}
