package de.telran.urlshortener.exception;

public class UserNameAlreadyTakenException extends RuntimeException {
    public UserNameAlreadyTakenException(String message) {
        super(message);
    }
}

