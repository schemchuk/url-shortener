package de.telran.urlshortener.dto.RoleDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleResponse {
    private Long id;
    private String name;
}