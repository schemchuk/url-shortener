package de.telran.urlshortener.service.impl;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.Role.RoleName;
import de.telran.urlshortener.exception.RoleException;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Импортируйте этот класс для логирования
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j // Добавьте эту аннотацию для логирования
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse createRole(RoleRequest roleRequest) {
        RoleName roleName = convertToRoleName(roleRequest.getName());
        Role existingRole = roleRepository.findByName(roleName).orElse(null);

        if (existingRole != null) {
            throw new RoleException.RoleAlreadyExistsException("Role already exists with name: " + roleName);
        }

        try {
            Role newRole = Role.builder().name(roleName).build();
            Role savedRole = roleRepository.save(newRole); // save() должен работать, если новая роль не существует
            return RoleMapper.toRoleResponse(savedRole);
        } catch (Exception e) {
            log.error("Error while creating role: ", e);
            throw new RuntimeException("An error occurred while creating the role.", e);
        }
    }


    @Override
    public RoleResponse getRoleByName(String roleName) {
        RoleName name = convertToRoleName(roleName);
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RoleException.RoleNotFoundException("Role not found with name: " + roleName));
        return RoleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RoleException.RoleNotFoundException("Role not found with id: " + id));
        roleRepository.delete(role);
    }

    private RoleName convertToRoleName(String roleName) {
        try {
            return RoleName.valueOf(roleName);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role name: " + roleName, e);
        }
    }
}










