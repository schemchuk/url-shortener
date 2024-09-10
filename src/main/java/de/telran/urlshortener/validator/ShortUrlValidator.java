package de.telran.urlshortener.validator;

import de.telran.urlshortener.exception.ShortUrlValidationException;

import java.util.regex.Pattern;

public class ShortUrlValidator {
    private static final String SHORT_URL_REGEX = "^[a-zA-Z0-9]{6}$";
    private static final Pattern SHORT_URL_PATTERN = Pattern.compile(SHORT_URL_REGEX);

    public static void validate(String shortUrl) {
        if (shortUrl == null || !SHORT_URL_PATTERN.matcher(shortUrl).matches()) {
            throw new ShortUrlValidationException("Short URL must be 6 characters long and contain only letters and digits");
        }
    }
}