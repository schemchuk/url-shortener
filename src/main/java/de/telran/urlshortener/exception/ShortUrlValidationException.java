package de.telran.urlshortener.exception;

public class ShortUrlValidationException extends RuntimeException {
    public ShortUrlValidationException(String message) {
        super(message);
    }
}

