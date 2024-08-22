package de.telran.urlshortener.exception;

public class RoleException extends RuntimeException {
    public RoleException(String message) {
        super(message);
    }

    public static class RoleNotFoundException extends RoleException {
        public RoleNotFoundException(String message) {
            super(message);
        }
    }

    public static class RoleAlreadyExistsException extends RoleException {
        public RoleAlreadyExistsException(String message) {
            super(message);
        }
    }
}

