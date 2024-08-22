package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.config.ShortUrlConfig;
import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.ShortUrlMapper;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class ShortUrlServiceImpl implements ShortUrlService {

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortUrlConfig shortUrlConfig;
    private final SecureRandom random = new SecureRandom(); // Используем SecureRandom для генерации случайных чисел

    @Override
    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

        String shortKey = generateShortKey(); // Генерация короткого ключа

        ShortUrl shortUrl = ShortUrl.builder()
                .shortKey(shortKey)
                .fullUrl(request.getFullUrl())
                .clickCount(0L) // Инициализация счетчика кликов
                .user(user) // Установка связи с пользователем
                .build();

        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);
        return ShortUrlMapper.toShortUrlResponse(savedShortUrl);
    }

    @Override
    public ShortUrlResponse getShortUrlByKey(String shortKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new RuntimeException("Short URL not found with key: " + shortKey));
        return ShortUrlMapper.toShortUrlResponse(shortUrl);
    }

    @Override
    public void incrementClickCount(String shortKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new RuntimeException("Short URL not found with key: " + shortKey));
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl);
    }

    @Override
    public void deleteShortUrl(Long id) {
        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Short URL not found with id: " + id));
        shortUrlRepository.delete(shortUrl);
    }

    private String generateShortKey() {
        StringBuilder key = new StringBuilder(shortUrlConfig.getKeyLength());
        String allowedCharacters = shortUrlConfig.getAllowedCharacters();
        int length = allowedCharacters.length();

        for (int i = 0; i < shortUrlConfig.getKeyLength(); i++) {
            int index = random.nextInt(length);
            key.append(allowedCharacters.charAt(index));
        }

        return key.toString();
    }
}



