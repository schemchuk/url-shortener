package de.telran.urlshortener.service.userservice;

import de.telran.urlshortener.dto.roledto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.entity.enums.SubscriptionType;
import de.telran.urlshortener.exception.UserNameAlreadyTakenException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.service.roleservice.RoleService;
import de.telran.urlshortener.service.subscriptionservice.SubscriptionService;
import de.telran.urlshortener.util.subscriptionserviceutil.SubscriptionUtil;
import de.telran.urlshortener.util.userroleserviceutil.UserRoleUtil;
import de.telran.urlshortener.validator.EmailValidator;
import de.telran.urlshortener.validator.PasswordValidator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final Logger log = LogManager.getLogger(UserService.class);

    private final UserLookupService userLookupService;
    private final UserPersistenceService userPersistenceService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final SubscriptionService subscriptionService;

    public User createUser(String userName, String email, String password) {
        log.info("Attempting to create user with username: {} and email: {}", userName, email);
        EmailValidator.validate(email);
        PasswordValidator.validate(password);

        if (userLookupService.getUserByEmail(email).isPresent()) {
            log.warn("Attempted to create user with already existing email: {}", email);
            throw new UserNameAlreadyTakenException("Email already in use");
        }

        User user = User.builder()
                .userName(userName)
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();

        User savedUser = userPersistenceService.save(user);

        RoleResponse trialRoleResponse = roleService.getRoleByName("TRIAL");
        Role trialRole = roleMapper.toRole(trialRoleResponse);

        UserRoleUtil.addRoleToUser(savedUser, trialRole);

        Subscription subscription = SubscriptionUtil.createSubscription(savedUser, SubscriptionType.TRIAL);
        subscriptionService.createSubscription(subscription);

        log.info("User created successfully with ID: {}", savedUser.getId());
        return savedUser;
    }

    public User updateUser(Long id, User user) {
        log.info("Attempting to update user with ID: {}", id);
        User existingUser = userLookupService.getUserById(id);

        existingUser.setUserName(user.getUserName());
        existingUser.setEmail(user.getEmail());
        if (user.getPassword() != null) {
            PasswordValidator.validate(user.getPassword());
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User updatedUser = userPersistenceService.save(existingUser);
        log.info("User with ID: {} updated successfully", updatedUser.getId());
        return updatedUser;
    }

    public void deleteUser(Long id) {
        log.info("Attempting to delete user with ID: {}", id);
        User user = userLookupService.getUserById(id);
        userPersistenceService.delete(user);
        log.info("User with ID: {} deleted successfully", id);
    }

    public User changeUserRole(Long userId, String newRoleName) {
        log.info("Attempting to change role for user with ID: {} to {}", userId, newRoleName);

        User user = userLookupService.getUserById(userId);
        RoleResponse newRoleResponse = roleService.getRoleByName(newRoleName);
        Role newRole = roleMapper.toRole(newRoleResponse);

        UserRoleUtil.removeAllRolesFromUser(user);
        UserRoleUtil.addRoleToUser(user, newRole);

        SubscriptionType newSubscriptionType = SubscriptionType.valueOf(newRoleName);
        Subscription existingSubscription = subscriptionService.getSubscriptionByUser(user);

        if (existingSubscription != null) {
            existingSubscription.setSubscriptionType(newSubscriptionType);
            subscriptionService.updateSubscription(existingSubscription);
        } else {
            Subscription newSubscription = SubscriptionUtil.createSubscription(user, newSubscriptionType);
            subscriptionService.createSubscription(newSubscription);
        }

        User updatedUser = userPersistenceService.save(user);

        if (updatedUser == null || updatedUser.getId() == null) {
            log.error("Failed to update user with ID: {}", user.getId());
            throw new RuntimeException("Failed to update user");
        }

        log.info("User role updated successfully with ID: {}", updatedUser.getId());
        return updatedUser;
    }
}