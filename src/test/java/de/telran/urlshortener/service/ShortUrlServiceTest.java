package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.ShortUrlNotFoundException;
import de.telran.urlshortener.mapper.ShortUrlMapper;
import de.telran.urlshortener.repository.ShortUrlRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.util.shortUrlServiceUtil.ShortUrlUtil;
import de.telran.urlshortener.validator.UrlValidator;
import de.telran.urlshortener.validator.ShortUrlValidator;
import de.telran.urlshortener.service.ShortUrlService;
import de.telran.urlshortener.service.ShortUrlTrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShortUrlTrackingService shortUrlTrackingService;

    @Mock
    private ShortUrlMapper shortUrlMapper;

    @Mock
    private ShortUrlUtil shortUrlUtil;

    @InjectMocks
    private ShortUrlService shortUrlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShortUrl_ShouldReturnShortUrlResponse() {
        // Arrange
        ShortUrlRequest request = new ShortUrlRequest("http://example.com", "user@example.com");
        User user = User.builder().id(1L).email("user@example.com").build();
        String shortKey = "123ABc";
        ShortUrl shortUrl = ShortUrl.builder()
                .id(1L)
                .shortKey(shortKey)
                .fullUrl(request.getFullUrl())
                .clickCount(0L)
                .user(user)
                .build();
        ShortUrlResponse response = ShortUrlResponse.builder()
                .id(1L)
                .shortKey(shortKey)
                .fullUrl(request.getFullUrl())
                .clickCount(0L)
                .build();

        // Mocking
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(shortUrlUtil.generateUniqueKey()).thenReturn(shortKey);
        when(shortUrlRepository.save(any(ShortUrl.class))).thenReturn(shortUrl);
        when(shortUrlMapper.toShortUrlResponse(shortUrl)).thenReturn(response);

        // Act
        ShortUrlResponse result = shortUrlService.createShortUrl(request);

        // Assert
        assertEquals(response, result);
        verify(userRepository).findByEmail(request.getEmail());
        verify(shortUrlUtil).generateUniqueKey();
        verify(shortUrlRepository).save(any(ShortUrl.class));
        verify(shortUrlMapper).toShortUrlResponse(shortUrl);
    }

    @Test
    void getShortUrlByKey_ShouldReturnShortUrlResponse() {
        // Arrange
        String shortKey = "shortKey";
        ShortUrl shortUrl = ShortUrl.builder()
                .shortKey(shortKey)
                .fullUrl("http://example.com")
                .clickCount(0L)
                .build();
        ShortUrlResponse response = ShortUrlResponse.builder()
                .shortKey(shortKey)
                .fullUrl("http://example.com")
                .clickCount(0L)
                .build();

        when(shortUrlTrackingService.getShortUrlByKey(shortKey)).thenReturn(shortUrl);
        when(shortUrlMapper.toShortUrlResponse(shortUrl)).thenReturn(response);

        // Act
        ShortUrlResponse result = shortUrlService.getShortUrlByKey(shortKey);

        // Assert
        assertEquals(response, result);
        verify(shortUrlTrackingService).getShortUrlByKey(shortKey);
        verify(shortUrlTrackingService).incrementClickCount(shortUrl);
        verify(shortUrlMapper).toShortUrlResponse(shortUrl);
    }

    @Test
    void deleteShortUrl_ShouldThrowExceptionIfNotFound() {
        // Arrange
        Long id = 1L;
        when(shortUrlRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ShortUrlNotFoundException.class, () -> shortUrlService.deleteShortUrl(id));
        verify(shortUrlRepository).findById(id);
    }

    @Test
    void deleteShortUrl_ShouldDeleteShortUrlIfFound() {
        // Arrange
        Long id = 1L;
        ShortUrl shortUrl = ShortUrl.builder().id(id).build();
        when(shortUrlRepository.findById(id)).thenReturn(Optional.of(shortUrl));

        // Act
        shortUrlService.deleteShortUrl(id);

        // Assert
        verify(shortUrlRepository).findById(id);
        verify(shortUrlRepository).delete(shortUrl);
    }
}
