package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final Logger log = LogManager.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public RoleResponse getRoleByName(String roleName) {
        Role.RoleName roleNameEnum;
        try {
            roleNameEnum = Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Role name {} is invalid", roleName);
            throw new RuntimeException("Role not found with name: " + roleName);
        }

        Role role = roleRepository.findByName(roleNameEnum)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        return roleMapper.toRoleResponse(role);
    }

    @Override
    public RoleResponse updateRole(Long id, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        Role.RoleName newRoleName;
        try {
            newRoleName = Role.RoleName.valueOf(roleRequest.getRoleName().toUpperCase());
        } catch (IllegalArgumentException e) {
            log.error("Role name {} is invalid", roleRequest.getRoleName());
            throw new RuntimeException("Invalid role name: " + roleRequest.getRoleName());
        }

        existingRole.setName(newRoleName);

        Role updatedRole = roleRepository.save(existingRole);
        return roleMapper.toRoleResponse(updatedRole);
    }
}













