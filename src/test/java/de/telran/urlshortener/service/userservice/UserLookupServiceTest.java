package de.telran.urlshortener.service.userservice;

import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserLookupServiceTest {

    private UserLookupService userLookupService;
    private UserRepository repository;

    @BeforeEach
    public void init() {
        repository = Mockito.mock(UserRepository.class);
        userLookupService = new UserLookupService(repository);
    }

    @Test
    public void getUserById_whenUserExists() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        when(repository.findById(id)).thenReturn(Optional.of(user));

        User result = userLookupService.getUserById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(repository).findById(id);
    }

    @Test
    public void getUserById_whenUserDoesNotExist() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            userLookupService.getUserById(id);
        });

        assertEquals("User not found with id: " + id, thrown.getMessage());
        verify(repository).findById(id);
    }

    @Test
    public void getUserByEmail_whenUserExists() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));

        Optional<User> result = userLookupService.getUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(email, result.get().getEmail());
        verify(repository).findByEmail(email);
    }

    @Test
    public void getUserByEmail_whenUserDoesNotExist() {
        String email = "test@example.com";
        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        Optional<User> result = userLookupService.getUserByEmail(email);

        assertFalse(result.isPresent());
        verify(repository).findByEmail(email);
    }

    @Test
    public void getByLogin_whenUserExists() {
        String login = "testlogin";
        User user = new User();
        user.setEmail(login); // предположим, что логин - это email
        when(repository.findByEmail(login)).thenReturn(Optional.of(user));

        Optional<User> result = userLookupService.getByLogin(login);

        assertTrue(result.isPresent());
        assertEquals(login, result.get().getEmail());
        verify(repository).findByEmail(login);
    }

    @Test
    public void getByLogin_whenUserDoesNotExist() {
        String login = "testlogin";
        when(repository.findByEmail(login)).thenReturn(Optional.empty());

        Optional<User> result = userLookupService.getByLogin(login);

        assertFalse(result.isPresent());
        verify(repository).findByEmail(login);
    }
}
