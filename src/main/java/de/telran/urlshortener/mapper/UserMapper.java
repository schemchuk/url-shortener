package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionResponse;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.ShortUrl;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.enums.SubscriptionType;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(UserMapper::toRoleResponse)
                        .collect(Collectors.toSet()))
                .subscription(Optional.ofNullable(user.getSubscription())
                        .map(UserMapper::toSubscriptionResponse)
                        .orElse(null))
                .shortUrls(Optional.ofNullable(user.getShortUrls())
                        .stream()
                        .flatMap(Set::stream)
                        .map(UserMapper::toShortUrlResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static SubscriptionResponse toSubscriptionResponse(Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .startDate(subscription.getStartDate().toString())
                .endDate(subscription.getEndDate().toString())
                .subscriptionType(toSubscriptionDtoType(subscription.getSubscriptionType()))
                .build();
    }

    public static ShortUrlResponse toShortUrlResponse(ShortUrl shortUrl) {
        return ShortUrlResponse.builder()
                .id(shortUrl.getId())
                .shortKey(shortUrl.getShortKey())
                .fullUrl(shortUrl.getFullUrl())
                .build();
    }

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName()) // Переводим RoleName enum в строку
                .build();
    }

    public static String toSubscriptionDtoType(SubscriptionType entityType) {
        return entityType == null ? null : entityType.name();
    }
}













