package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionRequest;
import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;
import de.telran.urlshortener.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/user/{userId}")
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @PathVariable Long userId,
            @RequestBody SubscriptionRequest request) {
        SubscriptionResponse subscriptionResponse = subscriptionService.createSubscription(userId, request);
        return new ResponseEntity<>(subscriptionResponse, HttpStatus.CREATED);
    }
}

