package de.telran.urlshortener.exception;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
