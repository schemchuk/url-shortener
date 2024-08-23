package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Role.RoleName;
import de.telran.urlshortener.exception.RoleException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse getRoleByName(String roleName) {
        RoleName name = convertToRoleName(roleName);
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RoleException.RoleNotFoundException("Role not found with name: " + roleName));
        return RoleMapper.toRoleResponse(role);
    }

    private RoleName convertToRoleName(String roleName) {
        try {
            return RoleName.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName, e);
        }
    }
}

