package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
        UserResponse userResponse = userService.createUser(request);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserResponse userResponse = userService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<RoleResponse>> getUserRoles(@PathVariable Long userId) {
        Set<RoleResponse> roles = userService.getUserRoles(userId);
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @GetMapping("/role/{roleName}")
    public ResponseEntity<Set<UserResponse>> getUsersByRoleName(@PathVariable String roleName) {
        Set<UserResponse> users = userService.getUsersByRoleName(roleName);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserRequest request) {
        UserResponse userResponse = userService.updateUser(id, request);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}



