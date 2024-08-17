package de.telran.urlshortener.exception.exceptionUser;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}