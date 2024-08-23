package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.userDto.UserRequest;
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
                .shortUrls(user.getShortUrls().stream()
                        .map(ShortUrlMapper::toShortUrlResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static User mapToUser(UserRequest userRequest) {
        return User.builder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }
}



















