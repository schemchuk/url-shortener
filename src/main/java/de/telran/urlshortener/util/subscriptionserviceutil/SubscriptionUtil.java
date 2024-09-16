package de.telran.urlshortener.util.subscriptionserviceutil;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.entity.enums.SubscriptionType;

public class SubscriptionUtil {

    public static Subscription createSubscription(User user, SubscriptionType subscriptionType) {
        return Subscription.builder()
                .user(user)
                .subscriptionType(subscriptionType)
                .build();
    }
}