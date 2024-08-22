package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionRequest;
import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.mapper.SubscriptionMapper;
import de.telran.urlshortener.repository.SubscriptionRepository;
import de.telran.urlshortener.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        Subscription subscription = Subscription.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .subscriptionType(request.getSubscriptionType())
                .build();
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return SubscriptionMapper.toSubscriptionResponse(savedSubscription);
    }

    @Override
    public SubscriptionResponse getSubscriptionById(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow();
        return SubscriptionMapper.toSubscriptionResponse(subscription);
    }

    @Override
    public void deleteSubscription(Long subscriptionId) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        subscriptionRepository.delete(subscription);
    }
}


