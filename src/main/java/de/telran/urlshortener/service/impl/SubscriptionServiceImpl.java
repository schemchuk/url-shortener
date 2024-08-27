package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.SubscriptionRepository;
import de.telran.urlshortener.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Override
    public Subscription createSubscription(Subscription subscription) {
        if (subscription.getUser() == null || subscription.getUser().getId() == null) {
            throw new IllegalArgumentException("User must be saved before creating a subscription.");
        }
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscription(Subscription subscription) {
        // Update logic, assuming subscription exists and is valid
        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription getSubscriptionByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }
}
