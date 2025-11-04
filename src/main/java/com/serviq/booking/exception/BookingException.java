package com.serviq.booking.exception;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }

    public BookingException(String message, Throwable cause) {
        super(message, cause);
    }

    public static BookingException invalidStatus(String currentStatus, String requiredStatus) {
        return new BookingException(
                String.format("Cannot perform this operation. Current status: %s, Required: %s",
                        currentStatus, requiredStatus)
        );
    }

    public static BookingException cancellationNotAllowed() {
        return new BookingException("Booking cannot be cancelled within 24 hours of the appointment");
    }

    public static BookingException alreadyCancelled() {
        return new BookingException("Booking has already been cancelled");
    }

    public static BookingException alreadyCheckedOut() {
        return new BookingException("Booking has already been checked out");
    }

}
