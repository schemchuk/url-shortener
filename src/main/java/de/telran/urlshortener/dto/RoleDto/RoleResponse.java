package de.telran.urlshortener.dto.RoleDto;

import de.telran.urlshortener.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
    private Long id;
    private Role.RoleName name;
}