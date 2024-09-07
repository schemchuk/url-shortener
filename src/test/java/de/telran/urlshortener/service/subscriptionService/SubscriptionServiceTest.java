package de.telran.urlshortener.service.subscriptionService;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.repository.SubscriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @InjectMocks
    private SubscriptionService subscriptionService;

    private User user;
    private Subscription subscription;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .id(1L)
                .userName("testUser")
                .email("test@test.com")
                .build();

        subscription = Subscription.builder()
                .id(1L)
                .user(user)
                .subscriptionType(de.telran.urlshortener.entity.enums.SubscriptionType.TRIAL)
                .build();
    }

    @Test
    void createSubscription_WithValidUser_Success() {
        // Arrange
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        // Act
        subscriptionService.createSubscription(subscription);

        // Assert
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void createSubscription_WithoutUser_ThrowsException() {
        // Arrange
        subscription.setUser(null);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                subscriptionService.createSubscription(subscription));

        assertEquals("User must be saved before creating a subscription.", exception.getMessage());
        verify(subscriptionRepository, times(0)).save(subscription);
    }

    @Test
    void updateSubscription_Success() {
        // Arrange
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        // Act
        subscriptionService.updateSubscription(subscription);

        // Assert
        verify(subscriptionRepository, times(1)).save(subscription);
    }

    @Test
    void getSubscriptionByUser_Success() {
        // Arrange
        when(subscriptionRepository.findByUser(user)).thenReturn(subscription);

        // Act
        Subscription foundSubscription = subscriptionService.getSubscriptionByUser(user);

        // Assert
        assertEquals(subscription, foundSubscription);
        verify(subscriptionRepository, times(1)).findByUser(user);
    }

    @Test
    void getSubscriptionByUser_WithNonExistentUser_ReturnsNull() {
        // Arrange
        when(subscriptionRepository.findByUser(user)).thenReturn(null);

        // Act
        Subscription foundSubscription = subscriptionService.getSubscriptionByUser(user);

        // Assert
        assertEquals(null, foundSubscription);
        verify(subscriptionRepository, times(1)).findByUser(user);
    }
}
