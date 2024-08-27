package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleRequest;
import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final Logger log = LogManager.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

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

    private LocalDateTime calculateExpiryDate(Role.RoleName roleName) {
        LocalDateTime now = LocalDateTime.now();
        switch (roleName) {
            case ADMIN:
                return null; // Бессрочная подписка
            case TRIAL:
                return now.plusMonths(1); // 1 месяц
            case PAID:
                return now.plusYears(1); // 1 год
            default:
                throw new IllegalArgumentException("Unexpected role: " + roleName);
        }
    }
}


















