package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import org.springframework.stereotype.Component;

@Component
public class ShortUrlMapper {

    public static ShortUrlResponse toShortUrlResponse(ShortUrl shortUrl) {
        return ShortUrlResponse.builder()
                .id(shortUrl.getId())
                .shortKey(shortUrl.getShortKey())
                .fullUrl(shortUrl.getFullUrl())
                .clickCount(shortUrl.getClickCount())
                .build();
    }
}



