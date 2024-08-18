package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.exceptionUser.UserNameAlreadyTakenException;
import de.telran.urlshortener.exception.exceptionUser.UserNotFoundException;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        userRepository.findByUserName(request.getUserName()).ifPresent(existingUser -> {
            throw new UserNameAlreadyTakenException("Username is already taken");
        });

        User newUser = User.builder()
                .userName(request.getUserName())
                .password(request.getPassword())
                .email(request.getEmail())
                .build();

        User savedUser = userRepository.save(newUser);
        return toUserResponse(savedUser);
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        return toUserResponse(user);
    }

    @Transactional(readOnly = true)
    public Set<RoleResponse> getUserRoles(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return user.getRoles().stream()
                .map(ConversionUtils::toRoleResponse)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<UserResponse> getUsersByRoleName(String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found"));

        return role.getUsers().stream()
                .map(this::toUserResponse)
                .collect(Collectors.toSet());
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        user.setUserName(request.getUserName());
        user.setEmail(request.getEmail());

        User updatedUser = userRepository.save(user);
        return toUserResponse(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUserName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(ConversionUtils::toRoleResponse)
                        .collect(Collectors.toSet()))
                .subscription(user.getSubscription() != null ? ConversionUtils.toSubscriptionResponse(user.getSubscription()) : null)
                .shortUrls(user.getShortUrls().stream()
                        .map(ConversionUtils::toShortUrlResponse)
                        .collect(Collectors.toSet()))
                .build();
    }
}





