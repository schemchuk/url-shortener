package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.UserMapper;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.repository.UserRepository;
import de.telran.urlshortener.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = User.builder()
                .userName(userRequest.getUserName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .roles(new HashSet<>()) // Инициализация ролей
                .build();

        addRolesToUser(user, userRequest.getRoles());

        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserResponse(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse updateUser(Long userId, UserRequest userRequest) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUserName(userRequest.getUserName());
        user.setEmail(userRequest.getEmail());

        updateUserRoles(user, userRequest.getRoles());

        User updatedUser = userRepository.save(user);
        return UserMapper.mapToUserResponse(updatedUser);
    }

    private void addRolesToUser(User user, Set<String> roleNames) {
        if (roleNames != null) {
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(Role.RoleName.valueOf(roleName))
                        .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
                user.addRole(role);
            }
        }
    }

    private void updateUserRoles(User user, Set<String> roleNames) {
        // Убедимся, что коллекция инициализирована
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        } else {
            user.getRoles().clear(); // Очищаем существующие роли
        }

        addRolesToUser(user, roleNames);
    }
}





