package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;

public interface UserService {
    UserResponse createUser(UserRequest userRequest);
    UserResponse getUserById(Long userId);
    void deleteUser(Long userId);
    UserResponse updateUser(Long userId, UserRequest userRequest);
}



