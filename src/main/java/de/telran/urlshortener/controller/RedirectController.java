package de.telran.urlshortener.controller;

import de.telran.urlshortener.service.RedirectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/r")
@RequiredArgsConstructor
@Tag(name = "Redirect Controller", description = "Handles URL redirections")
public class RedirectController {

    private final RedirectService redirectService;

    /**
     * Redirects to the full URL based on the short key and tracks the click count.
     *
     * @param shortKey the short key used to find the full URL.
     * @return a RedirectView to the full URL.
     */
    @Operation(summary = "Redirect to Full URL", description = "Redirects to the full URL based on the short key and tracks the click count")
    @GetMapping("/{shortKey}")
    public RedirectView redirectToFullUrl(@PathVariable String shortKey) {
        String fullUrl = redirectService.getAndTrackFullUrl(shortKey);
        return new RedirectView(fullUrl);
    }
}
