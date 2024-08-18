package de.telran.urlshortener.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "shorturl")
public class ShortUrlConfig {

    private String allowedCharacters;
    private int keyLength;

    // Getters and setters
    public String getAllowedCharacters() {
        return allowedCharacters;
    }

    public void setAllowedCharacters(String allowedCharacters) {
        this.allowedCharacters = allowedCharacters;
    }

    public int getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(int keyLength) {
        this.keyLength = keyLength;
    }
}

