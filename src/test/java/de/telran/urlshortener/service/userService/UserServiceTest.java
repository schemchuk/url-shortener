package de.telran.urlshortener.service.userService;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.entity.enums.SubscriptionType;
import de.telran.urlshortener.exception.UserNameAlreadyTakenException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.service.roleService.RoleService;
import de.telran.urlshortener.service.subscriptionService.SubscriptionService;
import de.telran.urlshortener.util.subscriptionServiceUtil.SubscriptionUtil;
import de.telran.urlshortener.util.userRoleServiceUtil.UserRoleUtil;
import de.telran.urlshortener.validator.EmailValidator;
import de.telran.urlshortener.validator.PasswordValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserLookupService userLookupService;

    @Mock
    private UserPersistenceService userPersistenceService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleService roleService;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private SubscriptionService subscriptionService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createUser_shouldCreateNewUser_whenEmailIsValid() {
        String userName = "user";
        String email = "user@example.com";
        String password = "password";

        when(userLookupService.getUserByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        User userToSave = User.builder()
                .userName(userName)
                .email(email)
                .password("encodedPassword")
                .build();
        when(userPersistenceService.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        RoleResponse trialRoleResponse = new RoleResponse(1L, "TRIAL", null);
        Role trialRole = Role.builder().id(1L).name(Role.RoleName.TRIAL).build();
        when(roleService.getRoleByName("TRIAL")).thenReturn(trialRoleResponse);
        when(roleMapper.toRole(trialRoleResponse)).thenReturn(trialRole);

        User createdUser = userService.createUser(userName, email, password);

        verify(userLookupService).getUserByEmail(email);
        verify(userPersistenceService).save(any(User.class));
        assertEquals("user", createdUser.getUserName());
        assertEquals("user@example.com", createdUser.getEmail());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    public void createUser_shouldThrowException_whenEmailAlreadyExists() {
        String email = "existing@example.com";

        when(userLookupService.getUserByEmail(email)).thenReturn(Optional.of(new User()));

        assertThrows(UserNameAlreadyTakenException.class, () -> userService.createUser("user", email, "password"));

        verify(userPersistenceService, never()).save(any(User.class));
    }

    @Test
    public void updateUser_shouldUpdateExistingUser_whenUserExists() {
        Long userId = 1L;
        User existingUser = User.builder().id(userId).userName("oldUser").email("old@example.com").build();
        User updatedData = User.builder().userName("newUser").email("new@example.com").password("newPassword").build();

        when(userLookupService.getUserById(userId)).thenReturn(existingUser);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userPersistenceService.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updatedUser = userService.updateUser(userId, updatedData);

        verify(userLookupService).getUserById(userId);
        verify(userPersistenceService).save(existingUser);

        assertEquals("newUser", updatedUser.getUserName());
        assertEquals("new@example.com", updatedUser.getEmail());
        assertEquals("encodedNewPassword", updatedUser.getPassword());
    }

    @Test
    public void deleteUser_shouldDeleteUser_whenUserExists() {
        Long userId = 1L;
        User existingUser = User.builder().id(userId).build();

        when(userLookupService.getUserById(userId)).thenReturn(existingUser);

        userService.deleteUser(userId);

        verify(userLookupService).getUserById(userId);
        verify(userPersistenceService).delete(existingUser);
    }

    @Test
    public void changeUserRole_shouldChangeRole_whenUserAndRoleExist() {
        // Arrange
        Long userId = 1L;
        String newRoleName = "PAID";
        User user = new User();
        user.setId(userId);

        RoleResponse roleResponse = new RoleResponse(1L, newRoleName, null);
        Role newRole = Role.builder().id(1L).name(Role.RoleName.PAID).build();

        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(SubscriptionType.TRIAL);

        when(userLookupService.getUserById(userId)).thenReturn(user);
        when(roleService.getRoleByName(newRoleName)).thenReturn(roleResponse);
        when(roleMapper.toRole(roleResponse)).thenReturn(newRole);
        when(subscriptionService.getSubscriptionByUser(user)).thenReturn(subscription);
        when(userPersistenceService.save(user)).thenReturn(user);

        // Act
        User updatedUser = userService.changeUserRole(userId, newRoleName);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        verify(userPersistenceService, times(1)).save(user);
        verify(subscriptionService, times(1)).updateSubscription(subscription);
    }
}
