package de.telran.urlshortener.service;

import de.telran.urlshortener.config.ShortUrlConfig;
import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.exceptionUrlshortener.ShortUrlNotFoundException;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortUrlConfig shortUrlConfig;

    @Transactional
    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String shortKey = generateShortKey();

        ShortUrl shortUrl = ShortUrl.builder()
                .shortKey(shortKey)
                .fullUrl(request.getFullUrl())
                .clickCount(0L)
                .creationDate(LocalDateTime.now())
                .user(user)
                .build();

        shortUrlRepository.save(shortUrl);

        return ShortUrlResponse.builder()
                .id(shortUrl.getId())
                .shortKey(shortUrl.getShortKey())
                .fullUrl(shortUrl.getFullUrl())
                .clickCount(shortUrl.getClickCount())
                .creationDate(shortUrl.getCreationDate().toString())
                .build();
    }


    @Transactional(readOnly = true)
    public ShortUrlResponse getShortUrlByKey(String shortKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found with key: " + shortKey));

        return ShortUrlResponse.builder()
                .id(shortUrl.getId())
                .shortKey(shortUrl.getShortKey())
                .fullUrl(shortUrl.getFullUrl())
                .clickCount(shortUrl.getClickCount())
                .creationDate(shortUrl.getCreationDate().toString())
                .build();
    }

    private String generateShortKey() {
        String allowedCharacters = shortUrlConfig.getAllowedCharacters();
        int keyLength = shortUrlConfig.getKeyLength();
        Random random = new Random();
        StringBuilder shortKey = new StringBuilder(keyLength);

        for (int i = 0; i < keyLength; i++) {
            int index = random.nextInt(allowedCharacters.length());
            shortKey.append(allowedCharacters.charAt(index));
        }

        String generatedKey = shortKey.toString();
        if (generatedKey.isEmpty()) {
            throw new IllegalStateException("Generated short key is empty");
        }

        return generatedKey;
    }

}



