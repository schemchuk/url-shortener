package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.service.UrlShortenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UrlController {

    private final UrlShortenerService service;

    @PostMapping("/createUrl")
    public ResponseEntity<ShortUrlResponse> createUrl(@RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = service.createShortUrl(request.getUrl());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{key}")
    public ResponseEntity<ShortUrlResponse> redirect(@PathVariable String key) {
        ShortUrlResponse response = service.getFullUrl(key);
        return ResponseEntity.ok(response);
    }
}

