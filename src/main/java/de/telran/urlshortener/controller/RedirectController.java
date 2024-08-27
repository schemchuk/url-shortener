package de.telran.urlshortener.controller;

import de.telran.urlshortener.service.ShortUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/r")
@RequiredArgsConstructor
public class RedirectController {

    private final ShortUrlService shortUrlService;

    @GetMapping("/{shortKey}")
    public RedirectView redirectToFullUrl(@PathVariable String shortKey) {
        String fullUrl = shortUrlService.getFullUrlByKey(shortKey);
        return new RedirectView(fullUrl);
    }
}
