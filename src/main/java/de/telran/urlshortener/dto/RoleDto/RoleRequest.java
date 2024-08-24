package de.telran.urlshortener.dto.RoleDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {
    private String roleName; // Должно быть строкой, соответствующей RoleName в сущности
}
