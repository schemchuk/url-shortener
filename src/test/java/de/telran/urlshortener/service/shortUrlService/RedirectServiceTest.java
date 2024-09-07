package de.telran.urlshortener.service.shortUrlService;

import de.telran.urlshortener.entity.ShortUrl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedirectServiceTest {

    @Mock
    private ShortUrlTrackingService shortUrlTrackingService;

    @InjectMocks
    private RedirectService redirectService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAndTrackFullUrl() {
        // Arrange
        String shortKey = "123ABc";
        String fullUrl = "http://example.com";
        ShortUrl shortUrl = new ShortUrl();
        shortUrl.setFullUrl(fullUrl);

        when(shortUrlTrackingService.getShortUrlByKey(shortKey)).thenReturn(shortUrl);

        // Act
        String result = redirectService.getAndTrackFullUrl(shortKey);

        // Assert
        verify(shortUrlTrackingService).incrementClickCount(shortUrl);
        assertEquals(fullUrl, result);
    }
}