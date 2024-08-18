package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.exception.exceptionRole.RoleNotFoundException;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.util.ConversionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleResponse createRole(RoleRequest request) {
        Role newRole = Role.builder()
                .name(request.getName())
                .build();

        Role savedRole = roleRepository.save(newRole);
        return ConversionUtils.toRoleResponse(savedRole);
    }

    public RoleResponse getRoleByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RoleNotFoundException("Role not found with name: " + name));

        return ConversionUtils.toRoleResponse(role);
    }
}






