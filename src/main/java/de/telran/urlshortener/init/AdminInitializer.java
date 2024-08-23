package de.telran.urlshortener.init;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Bean
    public ApplicationRunner initializer() {
        return args -> {
            Role adminRole = roleRepository.findByName(Role.RoleName.ADMIN)
                    .orElseThrow(() -> new RuntimeException("Admin role not found"));

            if (!userRepository.existsByEmail("admin@example.com")) {
                User adminUser = User.builder()
                        .userName("admin")
                        .email("admin@example.com")
                        .password("admin") // Убедитесь, что пароль будет закодирован
                        .build();
                adminUser.addRole(adminRole);

                userRepository.save(adminUser);
            }
        };
    }
}






