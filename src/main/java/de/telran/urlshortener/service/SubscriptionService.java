package de.telran.urlshortener.service;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public Subscription createSubscription(Subscription subscription) {
        if (subscription.getUser() == null || subscription.getUser().getId() == null) {
            throw new IllegalArgumentException("User must be saved before creating a subscription.");
        }
        return subscriptionRepository.save(subscription);
    }

    public Subscription updateSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public Subscription getSubscriptionByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }
}

