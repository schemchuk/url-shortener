package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/short-urls")
@RequiredArgsConstructor
public class ShortUrlController {

    private static final Logger log = LogManager.getLogger(ShortUrlController.class);

    private final ShortUrlService shortUrlService;

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody ShortUrlRequest request) {
        log.info("Creating short URL for: {}", request.getFullUrl());
        ShortUrlResponse response = shortUrlService.createShortUrl(request);
        log.info("Short URL created successfully with key: {}", response.getShortKey());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<ShortUrlResponse> getShortUrlByKey(@PathVariable String shortKey) {
        log.info("Fetching short URL with key: {}", shortKey);
        ShortUrlResponse response = shortUrlService.getShortUrlByKey(shortKey);
        log.info("Short URL fetched successfully: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable Long id) {
        log.info("Deleting short URL with ID: {}", id);
        shortUrlService.deleteShortUrl(id);
        log.info("Short URL with ID: {} deleted successfully", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}




