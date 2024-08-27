package de.telran.urlshortener.validator;

import java.util.regex.Pattern;
import de.telran.urlshortener.exception.EmailValidationException;

public class EmailValidator {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public static void validate(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new EmailValidationException("Invalid email format");
        }
    }
}
