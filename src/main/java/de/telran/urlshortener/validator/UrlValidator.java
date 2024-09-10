package de.telran.urlshortener.validator;

import de.telran.urlshortener.exception.UrlValidationException;
import java.net.URL;

public class UrlValidator {

    public static void validate(String url) {
        try {
            new URL(url).toURI(); // This checks for URL format and validity
        } catch (Exception e) {
            throw new UrlValidationException("Invalid URL format");
        }
    }
}