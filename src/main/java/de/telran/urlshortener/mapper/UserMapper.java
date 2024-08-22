package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(RoleMapper::toRoleResponse)
                        .collect(Collectors.toSet()))
                .subscription(user.getSubscription() != null ? SubscriptionMapper.toSubscriptionResponse(user.getSubscription()) : null)
                .shortUrls(user.getShortUrls() != null ? user.getShortUrls().stream()
                        .map(ShortUrlMapper::toShortUrlResponse)
                        .collect(Collectors.toSet()) : null)
                .build();
    }
}
















