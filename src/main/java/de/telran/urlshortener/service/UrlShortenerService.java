package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.Urls;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.util.ShortUrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UrlShortenerService {

    private final ShortUrlRepository repository;
    private final ShortUrlUtil util;

    public ShortUrlResponse createShortUrl(String fullUrl) {
        Urls existingShortUrl = repository.findByFullUrl(fullUrl);

        if (existingShortUrl != null) {
            return ShortUrlResponse.builder()
                    .key(existingShortUrl.getKey())
                    .fullUrl(existingShortUrl.getFullUrl())
                    .build();
        } else {
            String newKey = util.generateUniqueKey();
            Urls newEntity = Urls.builder()
                    .key(newKey)
                    .fullUrl(fullUrl)
                    .clickCount(0L)
                    .build();
            repository.save(newEntity);
            return ShortUrlResponse.builder()
                    .key(newKey)
                    .fullUrl(fullUrl)
                    .build();
        }
    }

    public ShortUrlResponse getFullUrl(String key) {
        Urls entityInDb = repository.findByKey(key);

        if (entityInDb == null) {
            // Return some kind of 404 response or custom error handling
            throw new RuntimeException("URL not found");
        }

        entityInDb.setClickCount(entityInDb.getClickCount() + 1);
        repository.save(entityInDb);
        return ShortUrlResponse.builder()
                .key(entityInDb.getKey())
                .fullUrl(entityInDb.getFullUrl())
                .build();
    }
}


