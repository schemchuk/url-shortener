package de.telran.urlshortener.service.userservice;

import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserPersistenceServiceTest {

    private UserPersistenceService userPersistenceService;
    private UserRepository userRepository;

    @BeforeEach
    public void init() {
        userRepository = Mockito.mock(UserRepository.class);
        userPersistenceService = new UserPersistenceService(userRepository);
    }

    @Test
    public void save_shouldReturnSavedUser() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userPersistenceService.save(user);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    public void delete_shouldDeleteUser() {
        User user = new User();
        user.setEmail("test@example.com");

        userPersistenceService.delete(user);

        verify(userRepository).delete(user);
    }

    @Test
    public void existsByEmail_shouldReturnTrueIfUserExists() {
        String email = "test@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean exists = userPersistenceService.existsByEmail(email);

        assertTrue(exists);
        verify(userRepository).existsByEmail(email);
    }

    @Test
    public void existsByEmail_shouldReturnFalseIfUserDoesNotExist() {
        String email = "nonexistent@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        boolean exists = userPersistenceService.existsByEmail(email);

        assertFalse(exists);
        verify(userRepository).existsByEmail(email);
    }
}
