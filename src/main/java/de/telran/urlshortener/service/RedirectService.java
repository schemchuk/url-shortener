package de.telran.urlshortener.service;

import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.exception.ShortUrlNotFoundException;
import de.telran.urlshortener.repository.ShortUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedirectService {

    private final ShortUrlRepository shortUrlRepository;

    public String getAndTrackFullUrl(String shortKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortKey(shortKey)
                .orElseThrow(() -> new ShortUrlNotFoundException("Short URL not found with key: " + shortKey));

        incrementClickCount(shortUrl);

        return shortUrl.getFullUrl();
    }

    private void incrementClickCount(ShortUrl shortUrl) {
        shortUrl.setClickCount(shortUrl.getClickCount() + 1);
        shortUrlRepository.save(shortUrl);
    }
}
