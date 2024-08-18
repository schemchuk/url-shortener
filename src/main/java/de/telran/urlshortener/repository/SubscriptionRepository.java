package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
}

