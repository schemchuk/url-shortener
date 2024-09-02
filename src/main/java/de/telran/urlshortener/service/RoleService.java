package de.telran.urlshortener.service;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    private Date calculateExpiryDate(Role.RoleName roleName) {
        Date now = new Date();
        switch (roleName) {
            case ADMIN:
                return null; // Бессрочная подписка
            case TRIAL:
                return new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000); // 1 месяц
            case PAID:
                return new Date(now.getTime() + 365L * 24 * 60 * 60 * 1000); // 1 год
            default:
                throw new IllegalArgumentException("Unexpected role: " + roleName);
        }
    }
}
