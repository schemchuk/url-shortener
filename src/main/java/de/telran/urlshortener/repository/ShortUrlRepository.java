package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.ShortUrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrlEntity, Long> {
    Optional<ShortUrlEntity> findByKey(String key);
    Optional<ShortUrlEntity> findByFullUrl(String fullUrl);
}

