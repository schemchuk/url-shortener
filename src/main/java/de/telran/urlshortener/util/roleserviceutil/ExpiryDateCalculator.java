package de.telran.urlshortener.util.roleserviceutil;

import de.telran.urlshortener.entity.Role;

import java.util.Date;

public class ExpiryDateCalculator {

    public static Date calculateExpiryDate(Role.RoleName roleName) {
        Date now = new Date();
        switch (roleName) {
            case ADMIN:
                return new Date(now.getTime() + 3L * 365 * 24 * 60 * 60 * 1000); // 3 года
            case TRIAL:
                return new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000); // 1 месяц
            case PAID:
                return new Date(now.getTime() + 365L * 24 * 60 * 60 * 1000); // 1 год
            default:
                throw new IllegalArgumentException("Unexpected role: " + roleName);
        }
    }
}
