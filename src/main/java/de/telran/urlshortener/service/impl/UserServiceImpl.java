package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.enums.SubscriptionType;
import de.telran.urlshortener.exception.UserNameAlreadyTakenException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.mapper.UserMapper;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.service.RoleService;
import de.telran.urlshortener.service.SubscriptionService;
import de.telran.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final SubscriptionService subscriptionService;

    @Override
    public User createUser(String userName, String email, String password) {
        log.info("Attempting to create user with username: {} and email: {}", userName, email);
        validateEmail(email);
        validatePassword(password);

        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        // Сначала сохранить пользователя
        User savedUser = userRepository.save(user);

        // Fetch the TRIAL role and assign it to the user
        RoleResponse trialRoleResponse = roleService.getRoleByName("TRIAL");
        Role trialRole = roleMapper.toRole(trialRoleResponse);

        savedUser.addRole(trialRole); // Использовать сохраненного пользователя

        // Создание подписки для пользователя
        Subscription subscription = Subscription.builder()
                .user(savedUser)  // Использовать сохраненного пользователя
                .subscriptionType(SubscriptionType.TRIAL)
                .build();
        subscriptionService.createSubscription(subscription);  // Сохранение подписки

        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Override
    public User updateUser(Long id, User user) {
        log.info("Attempting to update user with ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("User with ID: {} updated successfully", updatedUser.getId());
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
        log.info("User with ID: {} deleted successfully", id);
    }

    @Override
    public User getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        log.info("User with ID: {} fetched successfully", id);
        return user;
    }

    @Override
    public User changeUserRole(Long userId, String newRoleName) {
        log.info("Attempting to change role for user with ID: {} to {}", userId, newRoleName);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        RoleResponse newRoleResponse = roleService.getRoleByName(newRoleName);
        Role newRole = roleMapper.toRole(newRoleResponse);

        // Remove all existing roles and add the new role
        user.getRoles().clear();
        user.addRole(newRole);

        // Update user subscription based on the new role
        SubscriptionType newSubscriptionType = SubscriptionType.valueOf(newRoleName);
        Subscription existingSubscription = subscriptionService.getSubscriptionByUser(user);

        if (existingSubscription != null) {
            existingSubscription.setSubscriptionType(newSubscriptionType);
            subscriptionService.updateSubscription(existingSubscription);
        } else {
            Subscription newSubscription = Subscription.builder()
                    .user(user)
                    .subscriptionType(newSubscriptionType)
                    .build();
            subscriptionService.createSubscription(newSubscription);
        }

        User updatedUser = userRepository.save(user);
        log.info("User role updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("Attempted to create user with already existing email: {}", email);
            throw new UserNameAlreadyTakenException("Email already in use");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            log.warn("Attempted to create user with weak password");
            throw new RuntimeException("Password must be at least 8 characters long");
        }
    }
}
