package de.telran.urlshortener.config;

import jakarta.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        System.out.println("Allowed Characters: " + allowedCharacters);
        System.out.println("Key Length: " + keyLength);
    }
}
