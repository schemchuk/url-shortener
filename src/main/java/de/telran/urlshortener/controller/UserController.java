package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.UserMapper;
import de.telran.urlshortener.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Manages operations related to users")
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Create User", description = "Creates a new user")
    @PostMapping
    public UserResponse createUser(@RequestBody UserRequest userRequest) {
        log.info("Received request to create user with username: {}", userRequest.getUserName());
        User user = userMapper.mapToUser(userRequest);
        User createdUser = userService.createUser(user.getUserName(), user.getEmail(), user.getPassword());
        log.info("User created successfully with ID: {}", createdUser.getId());
        return userMapper.mapToUserResponse(createdUser);
    }

    @Operation(summary = "Get User by ID", description = "Fetches a user by their ID")
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        log.info("Received request to get user with ID: {}", id);
        User user = userService.getUserById(id);
        log.info("User fetched successfully with ID: {}", id);
        return userMapper.mapToUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update User", description = "Updates the details of an existing user")
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        log.info("Received request to update user with ID: {}", id);
        User user = userMapper.mapToUser(userRequest);
        User updatedUser = userService.updateUser(id, user);
        log.info("User updated successfully with ID: {}", id);
        return userMapper.mapToUserResponse(updatedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete User", description = "Deletes a user by their ID")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        userService.deleteUser(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change User Role", description = "Changes the role of a user")
    @PatchMapping("/{id}/role")
    public UserResponse changeUserRole(@PathVariable Long id, @RequestParam String newRole) {
        log.info("Received request to change role for user with ID: {} to {}", id, newRole);
        User updatedUser = userService.changeUserRole(id, newRole);
        log.info("User role updated successfully with ID: {}", id);
        return userMapper.mapToUserResponse(updatedUser);
    }
}
