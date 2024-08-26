package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.exception.UserNameAlreadyTakenException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.service.RoleService;
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

        // Fetch the TRIAL role and assign it to the user
        RoleResponse trialRoleResponse = roleService.getRoleByName("TRIAL");
        Role trialRole = roleMapper.toRole(trialRoleResponse);  // Convert RoleResponse to Role

        user.addRole(trialRole);

        User savedUser = userRepository.save(user);
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

