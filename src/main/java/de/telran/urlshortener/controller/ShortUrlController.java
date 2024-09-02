package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.urlDto.ShortUrlRequest;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.service.ShortUrlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/short-urls")
@RequiredArgsConstructor
@Tag(name = "Short URL Controller", description = "Manages operations related to short URLs")
public class ShortUrlController {

    private static final Logger log = LogManager.getLogger(ShortUrlController.class);

    private final ShortUrlService shortUrlService;

    /**
     * Creates a new short URL from a full URL.
     *
     * @param request the request containing the full URL.
     * @return the response containing the short URL and key.
     */
    @Operation(summary = "Create Short URL", description = "Creates a new short URL from a full URL")
    @PostMapping
    public ResponseEntity<ShortUrlResponse> createShortUrl(@RequestBody ShortUrlRequest request) {
        log.info("Creating short URL for: {}", request.getFullUrl());
        ShortUrlResponse response = shortUrlService.createShortUrl(request);
        log.info("Short URL created successfully with key: {}", response.getShortKey());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Fetches the details of a short URL by its key.
     *
     * @param shortKey the short key of the URL.
     * @return the response containing the full URL and key.
     */
    @Operation(summary = "Get Short URL by Key", description = "Fetches the details of a short URL by its key")
    @GetMapping("/{shortKey}")
    public ResponseEntity<ShortUrlResponse> getShortUrlByKey(@PathVariable String shortKey) {
        log.info("Fetching short URL with key: {}", shortKey);
        ShortUrlResponse response = shortUrlService.getShortUrlByKey(shortKey);
        log.info("Short URL fetched successfully: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Deletes a short URL by its ID.
     *
     * @param id the ID of the short URL to be deleted.
     * @return a response entity with no content.
     */
    @Operation(summary = "Delete Short URL", description = "Deletes a short URL by its ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShortUrl(@PathVariable Long id) {
        log.info("Deleting short URL with ID: {}", id);
        shortUrlService.deleteShortUrl(id);
        log.info("Short URL with ID: {} deleted successfully", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
