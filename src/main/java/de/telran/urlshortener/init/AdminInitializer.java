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

            User adminUser = new User("admin", "admin", "admin@example.com");
            adminUser.addRole(adminRole);

            userRepository.save(adminUser);
        };
    }
}




