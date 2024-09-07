package de.telran.urlshortener.util.userRoleServiceUtil;

import de.telran.urlshortener.entity.Role;
import de.telran.urlshortener.entity.User;

import java.util.HashSet;

public class UserRoleUtil {

    public static void addRoleToUser(User user, Role role) {
        if (user.getRoles() == null) {
            user.setRoles(new HashSet<>());
        }
        user.getRoles().add(role);
    }

    public static void removeAllRolesFromUser(User user) {
        if (user.getRoles() != null) {
            user.getRoles().clear();
        }
    }
}