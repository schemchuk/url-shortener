package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.UserMapper;
import de.telran.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        log.info("Received request to create user with username: {}", userRequest.getUserName());
        User user = userMapper.mapToUser(userRequest);
        User createdUser = userService.createUser(user.getUserName(), user.getEmail(), user.getPassword());
        log.info("User created successfully with ID: {}", createdUser.getId());
        return userMapper.mapToUserResponse(createdUser);
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("Received request to get user with ID: {}", id);
        User user = userService.getUserById(id);
        log.info("User fetched successfully with ID: {}", id);
        return userMapper.mapToUserResponse(user);
    }

    @PreAuthorize("hasRole('PAID')")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        log.info("Received request to update user with ID: {}", id);
        User user = userMapper.mapToUser(userRequest);
        User updatedUser = userService.updateUser(id, user);
        log.info("User updated successfully with ID: {}", id);
        return userMapper.mapToUserResponse(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/role")
    public UserResponse changeUserRole(@PathVariable Long id, @RequestParam String newRole) {
        log.info("Received request to change role for user with ID: {} to {}", id, newRole);
        User updatedUser = userService.changeUserRole(id, newRole);
        log.info("User role updated successfully with ID: {}", id);
        return userMapper.mapToUserResponse(updatedUser);
    }
}
