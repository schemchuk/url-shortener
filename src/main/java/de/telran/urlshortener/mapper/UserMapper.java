package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.userdto.UserRequest;
import de.telran.urlshortener.dto.userdto.UserResponse;
import de.telran.urlshortener.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private RoleMapper roleMapper;

    public UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(roleMapper::toRoleResponse)
                        .collect(Collectors.toSet()))
                .build();
    }

    public User mapToUser(UserRequest userRequest) {
        return User.builder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .build();
    }
}