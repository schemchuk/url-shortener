package de.telran.urlshortener.validator;

import de.telran.urlshortener.exception.PasswordValidationException;

public class PasswordValidator {
    private static final int MIN_PASSWORD_LENGTH = 8;

    public static void validate(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new PasswordValidationException("Password must be at least 8 characters long");
        }
    }
}

