package de.telran.urlshortener.service.roleService;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testGetRoleByName_Success() {
        Role.RoleName roleNameEnum = Role.RoleName.ADMIN;
        Role role = new Role();
        role.setName(roleNameEnum);
        role.setExpiryDate(Date.valueOf(LocalDate.now().plusDays(30)));

        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setRoleName(roleNameEnum.name());
        roleResponse.setExpiryDate(role.getExpiryDate());

        when(roleRepository.findByName(roleNameEnum)).thenReturn(java.util.Optional.of(role));
        when(roleMapper.toRoleResponse(role)).thenReturn(roleResponse);

        // Act
        RoleResponse response = roleService.getRoleByName(roleNameEnum.name());

        // Assert
        assertNotNull(response);
        assertEquals(roleNameEnum.name(), response.getRoleName());
        assertEquals(role.getExpiryDate(), response.getExpiryDate());
        verify(roleRepository, times(1)).findByName(roleNameEnum);
        verify(roleMapper, times(1)).toRoleResponse(role);
    }

    @Test
    void testGetRoleByName_InvalidRoleName() {
        // Arrange
        String invalidRoleName = "INVALID_ROLE";

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.getRoleByName(invalidRoleName);
        });

        assertEquals("Role not found with name: " + invalidRoleName, exception.getMessage());
        verify(roleRepository, times(0)).findByName(any());
        verify(roleMapper, times(0)).toRoleResponse(any());
    }

    @Test
    void testGetRoleByName_RoleNotFound() {
        // Arrange
        Role.RoleName roleNameEnum = Role.RoleName.ADMIN;

        when(roleRepository.findByName(roleNameEnum)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            roleService.getRoleByName(roleNameEnum.name());
        });

        assertEquals("Role not found with name: " + roleNameEnum.name(), exception.getMessage());
        verify(roleMapper, times(0)).toRoleResponse(any());
    }
}
