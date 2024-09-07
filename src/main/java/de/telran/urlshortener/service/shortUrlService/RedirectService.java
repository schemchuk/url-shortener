package de.telran.urlshortener.service.shortUrlService;

import de.telran.urlshortener.entity.ShortUrl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlTrackingService shortUrlTrackingService;

    public String getAndTrackFullUrl(String shortKey) {
        ShortUrl shortUrl = shortUrlTrackingService.getShortUrlByKey(shortKey);

        shortUrlTrackingService.incrementClickCount(shortUrl);

        return shortUrl.getFullUrl();
    }
}
