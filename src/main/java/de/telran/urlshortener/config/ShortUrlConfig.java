package de.telran.urlshortener.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "shorturl")
@Getter
@Setter
public class ShortUrlConfig {
    private String allowedCharacters;
    private int keyLength;
}

