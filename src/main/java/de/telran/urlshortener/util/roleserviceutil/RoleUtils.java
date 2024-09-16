package de.telran.urlshortener.util.roleserviceutil;

import de.telran.urlshortener.entity.Role;

public class RoleUtils {
    public static Role.RoleName parseRoleName(String roleName) {
        try {
            return Role.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role name: " + roleName, e);
        }
    }
}