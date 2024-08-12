package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsthname(String userName);
    Optional<User> findByEmail(String email);
}
