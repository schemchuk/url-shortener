package de.telran.urlshortener.init;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.service.userService.UserPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class AdminandUserInitializerTest {

    @InjectMocks
    private AdminAndUserInitializer adminInitializer;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void initializer_doesNotCreateRoles_ifAlreadyExist() throws Exception {
        // Предопределенные роли
        Role adminRole = new Role(1L, Role.RoleName.ADMIN, new Date());
        Role trialRole = new Role(2L, Role.RoleName.TRIAL, new Date());
        Role paidRole = new Role(3L, Role.RoleName.PAID, new Date());

        // Мокаем поведение репозитория, роли уже существуют
        when(roleRepository.findByName(Role.RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByName(Role.RoleName.TRIAL)).thenReturn(Optional.of(trialRole));
        when(roleRepository.findByName(Role.RoleName.PAID)).thenReturn(Optional.of(paidRole));

        // Запускаем инициализацию
        adminInitializer.initializer().run(null);

        // Проверяем, что роли не создаются повторно
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void initializer_createsAdminAndUser_ifNotExists() throws Exception {
        Role adminRole = new Role(1L, Role.RoleName.ADMIN, new Date());
        Role trialRole = new Role(2L, Role.RoleName.TRIAL, new Date());

        when(roleRepository.findByName(Role.RoleName.ADMIN)).thenReturn(Optional.of(adminRole));
        when(roleRepository.findByName(Role.RoleName.TRIAL)).thenReturn(Optional.of(trialRole));

        // Мокаем существование пользователей
        when(userPersistenceService.existsByEmail("admin@example.com")).thenReturn(false);
        when(userPersistenceService.existsByEmail("user@example.com")).thenReturn(false);

        // Мокаем кодирование пароля
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Запускаем инициализацию
        adminInitializer.initializer().run(null);

        // Проверяем, что пользователи созданы
        verify(userPersistenceService).save(argThat(user -> user.getEmail().equals("admin@example.com")));
        verify(userPersistenceService).save(argThat(user -> user.getEmail().equals("user@example.com")));
    }
}
