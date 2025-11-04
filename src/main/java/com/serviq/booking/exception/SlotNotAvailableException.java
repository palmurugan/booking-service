package com.serviq.booking.exception;

public class SlotNotAvailableException extends RuntimeException {

    public SlotNotAvailableException(String message) {
        super(message);
    }

    public SlotNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
