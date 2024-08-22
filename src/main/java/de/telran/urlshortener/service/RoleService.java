package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;

public interface RoleService {
    RoleResponse createRole(RoleRequest roleRequest);
    RoleResponse getRoleByName(String roleName);
    void deleteRole(Long id);
}



