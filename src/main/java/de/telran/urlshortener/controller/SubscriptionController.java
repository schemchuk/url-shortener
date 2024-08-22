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

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(@RequestBody SubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.createSubscription(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable Long id) {
        SubscriptionResponse response = subscriptionService.getSubscriptionById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

