package de.telran.urlshortener.util;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionRequest;
import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.Role;

public class ConversionUtils {

    public static Subscription.SubscriptionType toSubscriptionEntityType(SubscriptionRequest.SubscriptionType dtoType) {
        if (dtoType == null) {
            return null;
        }
        switch (dtoType) {
            case TRIAL:
                return Subscription.SubscriptionType.TRIAL;
            case PAID:
                return Subscription.SubscriptionType.PAID;
            default:
                throw new IllegalArgumentException("Unknown subscription type: " + dtoType);
        }
    }

    public static SubscriptionResponse.SubscriptionType toSubscriptionDtoType(Subscription.SubscriptionType entityType) {
        if (entityType == null) {
            return null;
        }
        switch (entityType) {
            case TRIAL:
                return SubscriptionResponse.SubscriptionType.TRIAL;
            case PAID:
                return SubscriptionResponse.SubscriptionType.PAID;
            default:
                throw new IllegalArgumentException("Unknown subscription type: " + entityType);
        }
    }

    public static SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .startDate(subscription.getStartDate())
                .endDate(subscription.getEndDate())
                .subscriptionType(toSubscriptionDtoType(subscription.getSubscriptionType()))
                .build();
    }

    public static ShortUrlResponse toShortUrlResponse(ShortUrl shortUrl) {
        return ShortUrlResponse.builder()
                .key(shortUrl.getShortKey())
                .fullUrl(shortUrl.getFullUrl())
                .build();
    }

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}





