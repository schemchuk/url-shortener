package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;

public class RoleMapper {

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .roleName(role.getName().name())  // Получаем строковое значение из enum RoleName
                .build();
    }
}












