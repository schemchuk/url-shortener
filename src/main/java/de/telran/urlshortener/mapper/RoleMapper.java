package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toRole(RoleResponse roleResponse) {
        return Role.builder()
                .id(roleResponse.getId())
                .name(Role.RoleName.valueOf(roleResponse.getRoleName().toUpperCase()))
                .expiryDate(roleResponse.getExpiryDate())
                .build();
    }

    public RoleResponse toRoleResponse(Role role) {
        return new RoleResponse(role.getId(), role.getName().name(), role.getExpiryDate());
    }
}


























