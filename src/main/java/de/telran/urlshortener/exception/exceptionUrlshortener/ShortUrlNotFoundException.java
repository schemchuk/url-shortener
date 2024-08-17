package de.telran.urlshortener.exception.exceptionUrlshortener;


public class ShortUrlNotFoundException extends RuntimeException {
    public ShortUrlNotFoundException(String message) {
        super(message);
    }
}