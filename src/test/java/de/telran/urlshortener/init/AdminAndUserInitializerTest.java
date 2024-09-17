package de.telran.urlshortener.init;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.service.userservice.UserPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AdminAndUserInitializerTest {

    @InjectMocks
    private AdminAndUserInitializer adminInitializer;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUsersIfNotExists() throws Exception {
        // Мокаем существование ролей
        when(roleRepository.findByName(Role.RoleName.ADMIN))
                .thenReturn(Optional.of(new Role(1L, Role.RoleName.ADMIN, null)));
        when(roleRepository.findByName(Role.RoleName.TRIAL))
                .thenReturn(Optional.of(new Role(2L, Role.RoleName.TRIAL, null)));

        // Мокаем кодирование пароля
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Мокаем существование пользователей
        when(userPersistenceService.existsByEmail("admin@example.com")).thenReturn(false);
        when(userPersistenceService.existsByEmail("boss@string.com")).thenReturn(false);

        // Запускаем инициализацию
        adminInitializer.initializer().run(null);

        // Проверяем, что пользователи созданы
        verify(userPersistenceService).save(argThat(user -> user.getEmail().equals("admin@example.com")));
        verify(userPersistenceService).save(argThat(user -> user.getEmail().equals("boss@string.com")));
    }

    @Test
    void shouldNotCreateUsersIfAlreadyExists() throws Exception {
        // Мокаем существование ролей
        when(roleRepository.findByName(Role.RoleName.ADMIN))
                .thenReturn(Optional.of(new Role(1L, Role.RoleName.ADMIN, null)));
        when(roleRepository.findByName(Role.RoleName.TRIAL))
                .thenReturn(Optional.of(new Role(2L, Role.RoleName.TRIAL, null)));

        // Мокаем существование пользователей
        when(userPersistenceService.existsByEmail("admin@example.com")).thenReturn(true);
        when(userPersistenceService.existsByEmail("boss@string.com")).thenReturn(true);

        // Запускаем инициализацию
        adminInitializer.initializer().run(null);

        // Проверяем, что пользователи не созданы
        verify(userPersistenceService, never()).save(any());
    }
}
