package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.ShortUrlRequest;
import de.telran.urlshortener.dto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrlEntity;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.util.ShortUrlUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;
@Service
@RequiredArgsConstructor
public class UrlShortenerService {
    private final ShortUrlRepository repository;
    private final ShortUrlUtil util;

    public ShortUrlResponse createShortUrl(ShortUrlRequest request) {
        String fullUrl = request.getUrl();

        ShortUrlEntity existingShortUrl = repository.findByFullUrl(fullUrl);

        if(existingShortUrl != null) {
            return ShortUrlResponse.builder().key(existingShortUrl.getKey()).build();
        } else {
            String newKey = util.generateUniqueKey();
            ShortUrlEntity newEntity = ShortUrlEntity.builder()
                    .key(newKey).fullUrl(fullUrl).clickCount(0l)
                    .build();
            repository.save(newEntity);
            return ShortUrlResponse.builder().key(newKey).build();
        }
    }

    public RedirectView getFullUrl(String key) {
        ShortUrlEntity entityInDb = repository.findByKey(key);

        if (entityInDb == null) {
            throw new RuntimeException("Url not found");
        }

        entityInDb.setClickCount(entityInDb.getClickCount() + 1);
        repository.save(entityInDb);
        return new RedirectView(entityInDb.getFullUrl());
    }
}
