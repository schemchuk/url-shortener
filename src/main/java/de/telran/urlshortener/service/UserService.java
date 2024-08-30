package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.entity.enums.SubscriptionType;
import de.telran.urlshortener.exception.EmailValidationException;
import de.telran.urlshortener.exception.PasswordValidationException;
import de.telran.urlshortener.exception.UserNameAlreadyTakenException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.validator.EmailValidator;
import de.telran.urlshortener.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LogManager.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final SubscriptionService subscriptionService;

    public User createUser(String userName, String email, String password) {
        log.info("Attempting to create user with username: {} and email: {}", userName, email);
        EmailValidator.validate(email);
        PasswordValidator.validate(password);

        if (userRepository.existsByEmail(email)) {
            log.warn("Attempted to create user with already existing email: {}", email);
            throw new UserNameAlreadyTakenException("Email already in use");
        }

        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        User savedUser = userRepository.save(user);

        RoleResponse trialRoleResponse = roleService.getRoleByName("TRIAL");
        Role trialRole = roleMapper.toRole(trialRoleResponse);

        savedUser.addRole(trialRole);

        Subscription subscription = Subscription.builder()
                .user(savedUser)
                .subscriptionType(SubscriptionType.TRIAL)
                .build();
        subscriptionService.createSubscription(subscription);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    public User updateUser(Long id, User user) {
        log.info("Attempting to update user with ID: {}", id);
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null) {
            PasswordValidator.validate(user.getPassword());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        log.info("User with ID: {} updated successfully", updatedUser.getId());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
        log.info("User with ID: {} deleted successfully", id);
    }

    public User getUserById(Long id) {
        log.info("Fetching user with ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        log.info("User with ID: {} fetched successfully", id);
        return user;
    }

    public User changeUserRole(Long userId, String newRoleName) {
        log.info("Attempting to change role for user with ID: {} to {}", userId, newRoleName);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        RoleResponse newRoleResponse = roleService.getRoleByName(newRoleName);
        Role newRole = roleMapper.toRole(newRoleResponse);

        user.getRoles().clear();
        user.addRole(newRole);

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

    public Optional<User> getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);
        return userRepository.findByEmail(email);
    }

    public Optional<User> getByLogin(String login) {
        return userRepository.findByEmail(login);
    }
}

