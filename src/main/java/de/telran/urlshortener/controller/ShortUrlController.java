package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/short-urls")
@RequiredArgsConstructor
public class ShortUrlController {

    private final ShortUrlService shortUrlService;

    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody ShortUrlRequest request) {
        ShortUrlResponse response = shortUrlService.createShortUrl(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{shortKey}")
    public ResponseEntity<ShortUrlResponse> getShortUrlByKey(@PathVariable String shortKey) {
        ShortUrlResponse response = shortUrlService.getShortUrlByKey(shortKey);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable Long id) {
        shortUrlService.deleteShortUrl(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


