package de.telran.urlshortener.service.shorturlservice;

import de.telran.urlshortener.config.ShortUrlConfig;
import de.telran.urlshortener.dto.urldto.ShortUrlRequest;
import de.telran.urlshortener.dto.urldto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.ShortUrlNotFoundException;
import de.telran.urlshortener.mapper.ShortUrlMapper;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.util.shorturlserviceutil.ShortUrlUtil;
import de.telran.urlshortener.validator.UrlValidator;
import de.telran.urlshortener.validator.ShortUrlValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortUrlService {

    private static final Logger log = LogManager.getLogger(ShortUrlService.class);

    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;
    private final ShortUrlConfig shortUrlConfig;
    private final ShortUrlMapper shortUrlMapper;
    private final ShortUrlTrackingService shortUrlTrackingService;
    private final ShortUrlUtil shortUrlUtil;

    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
        log.info("Creating short URL for user email: {}", request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ShortUrlNotFoundException("User not found with email: " + request.getEmail()));

        UrlValidator.validate(request.getFullUrl());

        String shortKey = shortUrlUtil.generateUniqueKey();
        ShortUrlValidator.validate(shortKey);

        log.info("Generated short key: {}", shortKey);

        ShortUrl shortUrl = ShortUrl.builder()
                .shortKey(shortKey)
                .fullUrl(request.getFullUrl())
                .clickCount(0L)
                .user(user)
                .build();

        ShortUrl savedShortUrl = shortUrlRepository.save(shortUrl);
        log.info("Short URL created with ID: {}", savedShortUrl.getId());
        return shortUrlMapper.toShortUrlResponse(savedShortUrl);
    }

    public ShortUrlResponse getShortUrlByKey(String shortKey) {
        log.info("Fetching short URL with key: {}", shortKey);
        ShortUrl shortUrl = shortUrlTrackingService.getShortUrlByKey(shortKey);

        shortUrlTrackingService.incrementClickCount(shortUrl);

        log.info("Short URL fetched successfully with key: {}", shortKey);
        return shortUrlMapper.toShortUrlResponse(shortUrl);
    }

    public void deleteShortUrl(Long id) {
        log.info("Attempting to delete short URL with ID: {}", id);
        ShortUrl shortUrl = shortUrlRepository.findById(id)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found with ID: " + id));
        shortUrlRepository.delete(shortUrl);
        log.info("Short URL with ID: {} deleted successfully", id);
    }
}
