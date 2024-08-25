package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;

public interface RoleService {
    RoleResponse getRoleByName(String roleName);
    RoleResponse updateRole(Long id, RoleRequest roleRequest);
}











