package de.telran.urlshortener.controller;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/{name}")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String name) {
        RoleResponse response = roleService.getRoleByName(name);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

