package de.telran.urlshortener.exception.exceptionRole;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
}
