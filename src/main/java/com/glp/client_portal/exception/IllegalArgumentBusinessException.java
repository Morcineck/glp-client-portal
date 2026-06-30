package com.glp.client_portal.exception;

public class IllegalArgumentBusinessException extends RuntimeException {
    public IllegalArgumentBusinessException(String message) {
        super(message);
    }

    public IllegalArgumentBusinessException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
