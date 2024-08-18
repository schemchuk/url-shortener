package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shorturls")
@RequiredArgsConstructor
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(
            @RequestParam String fullUrl,
            @RequestParam String userEmail) {
        ShortUrlResponse shortUrlResponse = urlShortenerService.createShortUrl(fullUrl, userEmail);
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{key}")
    public ResponseEntity<ShortUrlResponse> getOriginalUrl(@PathVariable String key) {
        ShortUrlResponse shortUrlResponse = urlShortenerService.getOriginalUrl(key);
        return new ResponseEntity<>(shortUrlResponse, HttpStatus.OK);
    }
}

