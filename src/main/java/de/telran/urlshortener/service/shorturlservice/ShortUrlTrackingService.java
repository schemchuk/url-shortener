package de.telran.urlshortener.service.shorturlservice;

import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.exception.ShortUrlNotFoundException;
import de.telran.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortUrlTrackingService {

    private final ShortUrlRepository shortUrlRepository;

    private static final Logger log = LogManager.getLogger(ShortUrlTrackingService.class);

    public ShortUrl getShortUrlByKey(String shortKey) {
        return shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found with key: " + shortKey));
    }

    public void incrementClickCount(ShortUrl shortUrl) {
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl);
        log.info("Click count incremented successfully for short URL with key: {}", shortUrl.getShortKey());
    }
}

