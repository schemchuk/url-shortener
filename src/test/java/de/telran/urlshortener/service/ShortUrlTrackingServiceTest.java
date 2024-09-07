package de.telran.urlshortener.service;

import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.exception.ShortUrlNotFoundException;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.service.shortUrlService.ShortUrlTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ShortUrlTrackingServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlTrackingService shortUrlTrackingService;

    private ShortUrl existingShortUrl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        existingShortUrl = new ShortUrl();
        existingShortUrl.setShortKey("abc123");
        existingShortUrl.setFullUrl("http://example.com");
        existingShortUrl.setClickCount(0L);

        when(shortUrlRepository.findByShortKey("abc123")).thenReturn(Optional.of(existingShortUrl));
        when(shortUrlRepository.findByShortKey("nonexistent")).thenReturn(Optional.empty());
    }

    @Test
    void testGetShortUrlByKey_Found() {
        ShortUrl shortUrl = shortUrlTrackingService.getShortUrlByKey("abc123");
        assertEquals("http://example.com", shortUrl.getFullUrl());
        assertEquals(0L, shortUrl.getClickCount()); // Using Long to match the entity
    }

    @Test
    void testGetShortUrlByKey_NotFound() {
        assertThrows(ShortUrlNotFoundException.class, () ->
                shortUrlTrackingService.getShortUrlByKey("nonexistent"));
    }

    @Test
    void testIncrementClickCount() {
        shortUrlTrackingService.incrementClickCount(existingShortUrl);

        verify(shortUrlRepository, times(1)).save(argThat(shortUrl ->
                shortUrl.getShortKey().equals("abc123") && shortUrl.getClickCount() == 1L));

        assertEquals(1L, existingShortUrl.getClickCount()); // Using Long to match the entity
    }
}
