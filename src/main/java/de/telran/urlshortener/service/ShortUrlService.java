package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;

public interface ShortUrlService {
    ShortUrlResponse createShortUrl(ShortUrlRequest request);
    ShortUrlResponse getShortUrlByKey(String shortKey);
    void incrementClickCount(String shortKey);
    void deleteShortUrl(Long id);
}



