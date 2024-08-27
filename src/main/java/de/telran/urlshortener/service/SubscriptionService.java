package de.telran.urlshortener.service;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;

public interface SubscriptionService {
    Subscription createSubscription(Subscription subscription);
    Subscription updateSubscription(Subscription subscription);
    Subscription getSubscriptionByUser(User user);
}
