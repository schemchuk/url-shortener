package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;

public interface RoleService {
    RoleResponse getRoleByName(String roleName);
}



