package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.Subscription;
import de.telran.urlshortener.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByEndDateBefore(LocalDateTime date);
    Subscription findByUser(User user);
}
