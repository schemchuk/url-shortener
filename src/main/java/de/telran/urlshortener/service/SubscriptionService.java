package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionRequest;
import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(SubscriptionRequest request);
    SubscriptionResponse getSubscriptionById(Long subscriptionId);
    void deleteSubscription(Long subscriptionId);
}

