package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.roledto.RoleResponse;
import de.telran.urlshortener.service.roleservice.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@Tag(name = "Role Controller", description = "Handles operations related to roles")
public class RoleController {

    private final RoleService roleService;

    /**
     * Fetches a role by its name.
     *
     * @param roleName the name of the role to fetch.
     * @return the role response.
     */
    @Operation(summary = "Get Role by Name", description = "Fetches a role by its name")
    @GetMapping("/{roleName}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String roleName) {
        RoleResponse roleResponse = roleService.getRoleByName(roleName);
        return ResponseEntity.ok(roleResponse);
    }
}
