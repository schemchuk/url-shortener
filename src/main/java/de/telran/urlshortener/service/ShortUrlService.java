package de.telran.urlshortener.service;

import de.telran.urlshortener.config.ShortUrlConfig;
import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.ShortUrlMapper;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private static final Logger log = LogManager.getLogger(ShortUrlService.class);

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortUrlConfig shortUrlConfig;
    private final SecureRandom random = new SecureRandom();

    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
        log.info("Creating short URL for user email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> {
                    log.error("User not found with email: {}", request.getEmail());
                    return new RuntimeException("User not found with email: " + request.getEmail());
                });

        String shortKey = generateShortKey();
        log.info("Generated short key: {}", shortKey);

        ShortUrl shortUrl = ShortUrl.builder()
                .shortKey(shortKey)
                .fullUrl(request.getFullUrl())
                .creationDate(LocalDateTime.now())
                .clickCount(0L)
                .user(user)
                .build();

        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);
        log.info("Short URL created with ID: {}", savedShortUrl.getId());
        return ShortUrlMapper.toShortUrlResponse(savedShortUrl);
    }

    public ShortUrlResponse getShortUrlByKey(String shortKey) {
        log.info("Fetching short URL with key: {}", shortKey);
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> {
                    log.error("Short URL not found with key: {}", shortKey);
                    return new RuntimeException("Short URL not found with key: " + shortKey);
                });

        incrementClickCount(shortKey);

        log.info("Short URL fetched successfully with key: {}", shortKey);
        return ShortUrlMapper.toShortUrlResponse(shortUrl);
    }

    public void incrementClickCount(String shortKey) {
        log.info("Incrementing click count for short URL with key: {}", shortKey);
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> {
                    log.error("Short URL not found with key: {}", shortKey);
                    return new RuntimeException("Short URL not found with key: " + shortKey);
                });
        shortUrl.incrementClickCount();
        shortUrlRepository.save(shortUrl);
        log.info("Click count incremented successfully for short URL with key: {}", shortKey);
    }

    public String getFullUrlByKey(String shortKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new RuntimeException("Short URL not found with key: " + shortKey));
        return shortUrl.getFullUrl();
    }

    public void deleteShortUrl(Long id) {
        log.info("Attempting to delete short URL with ID: {}", id);
        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Short URL not found with ID: {}", id);
                    return new RuntimeException("Short URL not found with ID: " + id);
                });
        shortUrlRepository.delete(shortUrl);
        log.info("Short URL with ID: {} deleted successfully", id);
    }

    private String generateShortKey() {
        log.info("Generating short key with length: {}", shortUrlConfig.getKeyLength());
        StringBuilder key = new StringBuilder(shortUrlConfig.getKeyLength());
        String allowedCharacters = shortUrlConfig.getAllowedCharacters();
        int length = allowedCharacters.length();

        for (int i = 0; i < shortUrlConfig.getKeyLength(); i++) {
            int index = random.nextInt(length);
            key.append(allowedCharacters.charAt(index));
        }

        log.info("Generated short key: {}", key);
        return key.toString();
    }
}

