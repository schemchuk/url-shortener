package de.telran.urlshortener.init;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.service.userservice.UserPersistenceService;
import de.telran.urlshortener.util.userroleserviceutil.UserRoleUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminAndUserInitializer {

    private final UserPersistenceService userPersistenceService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            // Создание ролей, если они отсутствуют
            createRoleIfNotExists(Role.RoleName.ADMIN, null); // Бессрочно
            createRoleIfNotExists(Role.RoleName.TRIAL, getExpiryDate(30)); // 30 дней
            createRoleIfNotExists(Role.RoleName.PAID, getExpiryDate(365)); // 365 дней

            // Поиск ролей
            Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            Role trialRole = roleRepository.findByName(Role.RoleName.TRIAL)
                    .orElseThrow(() -> new RuntimeException("Trial role not found"));

            // Создание пользователей
            if (!userPersistenceService.existsByEmail("admin@example.com")) {
                User adminUser = User.builder()
                        .userName("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("adminpassword"))
                        .build();
                UserRoleUtil.addRoleToUser(adminUser, adminRole); // Использование UserRoleUtil
                userPersistenceService.save(adminUser);
            }

            if (!userPersistenceService.existsByEmail("user@example.com")) {
                User regularUser = User.builder()
                        .userName("user")
                        .email("user@example.com")
                        .password(passwordEncoder.encode("userpassword"))
                        .build();
                UserRoleUtil.addRoleToUser(regularUser, trialRole); // Использование UserRoleUtil
                userPersistenceService.save(regularUser);
            }
        };
    }

    void createRoleIfNotExists(Role.RoleName roleName, Date expiryDate) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(null, roleName, expiryDate)));
    }

    private Date getExpiryDate(int days) {
        long expiryTime = System.currentTimeMillis() + (long) days * 24 * 60 * 60 * 1000;
        return new Date(expiryTime);
    }
}
