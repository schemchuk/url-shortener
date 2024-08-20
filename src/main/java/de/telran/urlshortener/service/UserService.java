package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.userDto.UserRequest;
import de.telran.urlshortener.dto.userDto.UserResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.mapper.UserMapper;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.repository.SubscriptionRepository;
import de.telran.urlshortener.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import de.telran.urlshortener.enums.SubscriptionType;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        // Создаем роль TRIAL, если она еще не существует
        Role trialRole = roleRepository.findByName(Role.RoleName.TRIAL)
                .orElseGet(() -> roleRepository.save(new Role(Role.RoleName.TRIAL)));

        // Создаем подписку TRIAL с текущей датой начала и окончания через месяц
        Subscription subscription = Subscription.builder()
                .subscriptionType(SubscriptionType.TRIAL)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .build();
        subscriptionRepository.save(subscription);

        // Создаем пользователя
        User user = User.builder()
                .userName(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(trialRole)) // Назначаем роль TRIAL
                .subscription(subscription) // Назначаем подписку
                .build();

        userRepository.save(user);

        return UserMapper.mapToUserResponse(user);
    }

    @Transactional
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getShortUrls() == null) {
            user.setShortUrls(new HashSet<>());
        }

        return UserMapper.mapToUserResponse(user);
    }

    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.mapToUserResponse(user);
    }

    @Transactional
    public void assignRoleToUser(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Role.RoleName roleEnum;
        try {
            roleEnum = Role.RoleName.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            log.error("Invalid role name: {}", roleName);
            throw new RuntimeException("Invalid role name");
        }

        Optional<Role> existingRole = user.getRoles().stream()
                .filter(role -> role.getName() == roleEnum)
                .findFirst();

        if (existingRole.isEmpty()) {
            Role role = roleRepository.findByName(roleEnum)
                    .orElseGet(() -> roleRepository.save(new Role(roleEnum)));
            user.getRoles().add(role);
            userRepository.save(user);
            log.info("Assigned role {} to user {}", roleName, userId);
        } else {
            log.info("Role {} already assigned to user {}", roleName, userId);
        }
    }
}














