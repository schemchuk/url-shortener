package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testExistsByEmail_WhenEmailExists() {
        // Arrange: create a user with a non-null password
        User user = new User();
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password"); // Можно использовать простой текст, если не нужен хеш
        userRepository.save(user);

        // Act: check if a user with the given email exists
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert: verify that the method returns true
        assertTrue(exists);
    }

    @Test
    public void testExistsByEmail_WhenEmailDoesNotExist() {
        // Act: check if a user with a non-existing email exists
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Assert: verify that the method returns false
        assertFalse(exists);
    }
}
