package de.telran.urlshortener.exception;

public class InvalidJwtTokenException extends RuntimeException {
    public InvalidJwtTokenException(String message) {
        super(message);
    }
}
