package de.telran.urlshortener.service;

import de.telran.urlshortener.config.ShortUrlConfig;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.exceptionUrlshortener.ShortUrlNotFoundException;
import de.telran.urlshortener.exception.exceptionUser.UserNotFoundException;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

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

        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setFullUrl(fullUrl);
        shortUrl.setClickCount(0L);
        shortUrl.setCreationDate(LocalDateTime.now());
        shortUrl.setUser(user);

        String generatedShortUrl = generateShortUrl();
        shortUrl.setShortKey(generatedShortUrl);

        ShortUrl savedUrl = shortUrlRepository.save(shortUrl);
        return ConversionUtils.toShortUrlResponse(savedUrl);
    }

    @Transactional(readOnly = true)
    public ShortUrlResponse getOriginalUrl(String key) {
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(key)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found with key: " + key));

        return ConversionUtils.toShortUrlResponse(shortUrl);
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







