package com.fitplanner.authentication.exception;

public class InvalidAuthenticationTokenException extends RuntimeException {
    public InvalidAuthenticationTokenException(String message) {
        super(message);
    }
}
