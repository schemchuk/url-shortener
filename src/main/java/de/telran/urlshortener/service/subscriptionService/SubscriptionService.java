package de.telran.urlshortener.service.subscriptionService;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public void createSubscription(Subscription subscription) {
        if (subscription.getUser() == null || subscription.getUser().getId() == null) {
            throw new IllegalArgumentException("User must be saved before creating a subscription.");
        }
        subscriptionRepository.save(subscription);
    }

    public void updateSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    public Subscription getSubscriptionByUser(User user) {
        return subscriptionRepository.findByUser(user);
    }
}

