package de.telran.urlshortener.exception;

public class UrlValidationException extends RuntimeException {
    public UrlValidationException(String message) {
        super(message);
    }
}
