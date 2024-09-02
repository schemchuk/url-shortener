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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /**
     * Creates a new user.
     *
     * @param userRequest the user data from the request body.
     * @return the created user response.
     */
    @Operation(summary = "Create User", description = "Creates a new user")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userRequest) {
        log.info("Received request to create user with username: {}", userRequest.getUserName());
        try {
            User user = userMapper.mapToUser(userRequest);
            User createdUser = userService.createUser(user.getUserName(), user.getEmail(), user.getPassword());
            log.info("User created successfully with ID: {}", createdUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.mapToUserResponse(createdUser));
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Fetches a user by their ID.
     *
     * @param id the user ID.
     * @return the user response.
     */
    @Operation(summary = "Get User by ID", description = "Fetches a user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        log.info("Received request to get user with ID: {}", id);
        try {
            User user = userService.getUserById(id);
            log.info("User fetched successfully with ID: {}", id);
            return ResponseEntity.ok(userMapper.mapToUserResponse(user));
        } catch (Exception e) {
            log.error("Error fetching user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Updates the details of an existing user.
     *
     * @param id          the user ID.
     * @param userRequest the updated user data.
     * @return the updated user response.
     */
    @PreAuthorize("hasRole('ROLE_PAID')")
    @Operation(summary = "Update User", description = "Updates the details of an existing user")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        log.info("Received request to update user with ID: {}", id);
        try {
            User user = userMapper.mapToUser(userRequest);
            User updatedUser = userService.updateUser(id, user);
            log.info("User updated successfully with ID: {}", id);
            return ResponseEntity.ok(userMapper.mapToUserResponse(updatedUser));
        } catch (Exception e) {
            log.error("Error updating user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the user ID.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Delete User", description = "Deletes a user by their ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("Received request to delete user with ID: {}", id);
        try {
            userService.deleteUser(id);
            log.info("User deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting user with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Changes the role of a user.
     *
     * @param id       the user ID.
     * @param newRole  the new role.
     * @return the updated user response.
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(summary = "Change User Role", description = "Changes the role of a user")
    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponse> changeUserRole(@PathVariable Long id, @RequestParam String newRole) {
        log.info("Received request to change role for user with ID: {} to {}", id, newRole);
        try {
            User updatedUser = userService.changeUserRole(id, newRole);
            log.info("User role updated successfully with ID: {}", id);
            return ResponseEntity.ok(userMapper.mapToUserResponse(updatedUser));
        } catch (Exception e) {
            log.error("Error changing user role with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
