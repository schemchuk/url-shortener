package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.User;
import de.telran.urlshortener.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    Optional<User> findByEmail(String email);

    Optional<User> findByRoles_Name(Role.RoleName roleName);
}

