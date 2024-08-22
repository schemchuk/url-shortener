package de.telran.urlshortener.mapper;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;

public class RoleMapper {

    public static RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName()) // RoleName уже подходит, если используется RoleName в RoleResponse
                .build();
    }
}






