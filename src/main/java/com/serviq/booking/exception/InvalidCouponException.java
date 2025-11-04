package com.serviq.booking.exception;

public class InvalidCouponException extends RuntimeException {

    public InvalidCouponException(String message) {
        super(message);
    }

    public static InvalidCouponException notFound() {
        return new InvalidCouponException("Invalid coupon code");
    }

    public static InvalidCouponException expired() {
        return new InvalidCouponException("Coupon has expired");
    }

    public static InvalidCouponException minAmountNotMet(String minAmount) {
        return new InvalidCouponException("Minimum order amount of " + minAmount + " is required");
    }

    public static InvalidCouponException usageLimitExceeded() {
        return new InvalidCouponException("Coupon usage limit has been exceeded");
    }
}
