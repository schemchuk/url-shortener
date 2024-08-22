package de.telran.urlshortener.config;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Role.RoleName;
import de.telran.urlshortener.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppConfig implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElse(null);

        if (adminRole == null) {
            adminRole = Role.builder().name(RoleName.ADMIN).build();
            roleRepository.save(adminRole);
        }
    }
}






