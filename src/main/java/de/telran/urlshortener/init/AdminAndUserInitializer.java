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
            createRoleIfNotExists(Role.RoleName.ADMIN);
            createRoleIfNotExists(Role.RoleName.TRIAL);
            createRoleIfNotExists(Role.RoleName.PAID);

            // Создание пользователей
            createUserIfNotExists("admin@example.com", "admin", "adminpassword", Role.RoleName.ADMIN);
            createUserIfNotExists("boss@string.com", "boss", "123string", Role.RoleName.TRIAL);
        };
    }

    private void createRoleIfNotExists(Role.RoleName roleName) {
        roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(null, roleName, null)));
    }

    private void createUserIfNotExists(String email, String userName, String password, Role.RoleName roleName) {
        if (!userPersistenceService.existsByEmail(email)) {
            User user = User.builder()
                    .userName(userName)
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            UserRoleUtil.addRoleToUser(user, role);
            userPersistenceService.save(user);
        }
    }
}
