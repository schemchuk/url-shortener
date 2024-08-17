package de.telran.urlshortener.service;

import de.telran.urlshortener.config.ShortUrlConfig;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrlEntity;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.exceptionUrlshortener.ShortUrlNotFoundException;
import de.telran.urlshortener.exception.exceptionUser.UserNotFoundException;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortUrlConfig shortUrlConfig;


    @Transactional
    public ShortUrlResponse createShortUrl(String fullUrl, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + userEmail));

        ShortUrlEntity shortUrl = new ShortUrlEntity();
        shortUrl.setFullUrl(fullUrl);
        shortUrl.setClickCount(0L);
        shortUrl.setUser(user);

        String generatedShortUrl = generateShortUrl();
        shortUrl.setKey(generatedShortUrl);

        ShortUrlEntity savedUrl = shortUrlRepository.save(shortUrl);

        return new ShortUrlResponse(savedUrl.getKey(), savedUrl.getFullUrl());
    }

    @Transactional(readOnly = true)
    public ShortUrlResponse getFullUrl(String key) {
        ShortUrlEntity shortUrl = shortUrlRepository.findByKey(key)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found with key: " + key));

        return new ShortUrlResponse(shortUrl.getKey(), shortUrl.getFullUrl());
    }

    private String generateShortUrl() {
        int keyLength = shortUrlConfig.getKeyLength();
        String allowedCharacters = shortUrlConfig.getAllowedCharacters();

        SecureRandom random = new SecureRandom();
        StringBuilder shortUrl = new StringBuilder(keyLength);

        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(allowedCharacters.length());
            shortUrl.append(allowedCharacters.charAt(randomIndex));
        }

        return shortUrl.toString();
    }
}


