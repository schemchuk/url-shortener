package de.telran.urlshortener.service.roleService;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.mapper.RoleMapper;
import de.telran.urlshortener.repository.RoleRepository;
import de.telran.urlshortener.util.roleserviceUtil.RoleUtils; // Импортируем RoleUtils
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private static final Logger log = LogManager.getLogger(RoleService.class);

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public RoleResponse getRoleByName(String roleName) {
        Role.RoleName roleNameEnum;
        try {
            roleNameEnum = RoleUtils.parseRoleName(roleName);
            log.debug("Parsed role name: {}", roleNameEnum);
        } catch (IllegalArgumentException e) {
            log.error("Role name {} is invalid", roleName);
            throw new RuntimeException("Role not found with name: " + roleName);
        }

        Role role = roleRepository.findByName(roleNameEnum)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + roleName));

        log.debug("Found role: {}", role);

        return roleMapper.toRoleResponse(role);
    }
}
