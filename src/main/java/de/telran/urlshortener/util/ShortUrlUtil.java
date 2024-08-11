package de.telran.urlshortener.util;

import de.telran.urlshortener.config.ShortUrlConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class ShortUrlUtil {
    private final ShortUrlConfig config;
    @Autowired
    public String generateUniqueKey() {
        int keyLength = config.getKeyLength();
        String allowedCharacters = config.getAllowedCharacters();

        StringBuilder keyBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            keyBuilder.append(allowedCharacters.charAt(randomIndex));
        }
        return keyBuilder.toString();
    }
}
