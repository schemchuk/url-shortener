package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        // Создаем роли, если они еще не существуют
        if (!roleRepository.findByName(Role.RoleName.ADMIN).isPresent()) {
            Role adminRole = Role.builder()
                    .name(Role.RoleName.ADMIN)
                    .build();
            roleRepository.save(adminRole);
        }
        if (!roleRepository.findByName(Role.RoleName.TRIAL).isPresent()) {
            Role trialRole = Role.builder()
                    .name(Role.RoleName.TRIAL)
                    .build();
            roleRepository.save(trialRole);
        }
    }

    @Test
    public void testRoleExists() {
        // Проверяем, что роли существуют в тестовой базе данных
        assertThat(roleRepository.findByName(Role.RoleName.ADMIN)).isPresent();
        assertThat(roleRepository.findByName(Role.RoleName.TRIAL)).isPresent();
    }
}
