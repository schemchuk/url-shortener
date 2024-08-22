package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.enums.SubscriptionType;

public class SubscriptionMapper {

    public static SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .startDate(subscription.getStartDate().toString())
                .endDate(subscription.getEndDate().toString())
                .subscriptionType(toSubscriptionDtoType(subscription.getSubscriptionType()))
                .build();
    }

    public static String toSubscriptionDtoType(SubscriptionType entityType) {
        return entityType == null ? null : entityType.name();
    }
}


