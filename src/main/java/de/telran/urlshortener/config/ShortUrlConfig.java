package de.telran.urlshortener.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
@Getter
public class ShortUrlConfig {
    private String allowedCharacters;
    private int keyLength;
}
