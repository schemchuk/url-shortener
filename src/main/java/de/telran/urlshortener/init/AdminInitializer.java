package de.telran.urlshortener.init;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            // Создание ролей, если они отсутствуют
            createRoleIfNotExists(Role.RoleName.ADMIN, null);  // Устанавливаем null или задаем конкретное значение
            createRoleIfNotExists(Role.RoleName.TRIAL, new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));  // Пример с конкретной датой
            createRoleIfNotExists(Role.RoleName.PAID, new Date(System.currentTimeMillis() + 365L * 24 * 60 * 60 * 1000));  // Пример с конкретной датой

            // Поиск ролей
            Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));
            Role userRole = roleRepository.findByName(Role.RoleName.TRIAL)
                    .orElseThrow(() -> new RuntimeException("User role not found"));

            // Создание пользователей
            if (!userRepository.existsByEmail("admin@example.com")) {
                User adminUser = User.builder()
                        .userName("admin")
                        .email("admin@example.com")
                        .password(passwordEncoder.encode("adminpassword")) // Кодирование пароля
                        .build();
                adminUser.addRole(adminRole);
                userRepository.save(adminUser);
            }

            if (!userRepository.existsByEmail("user@example.com")) {
                User regularUser = User.builder()
                        .userName("user")
                        .email("user@example.com")
                        .password(passwordEncoder.encode("userpassword")) // Кодирование пароля
                        .build();
                regularUser.addRole(userRole);
                userRepository.save(regularUser);
            }
        };
    }

    private void createRoleIfNotExists(Role.RoleName roleName, Date expiryDate) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(null, roleName, expiryDate)));
    }
}
