package de.telran.urlshortener.repository;

import de.telran.urlshortener.entity.Urls;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortUrlRepository extends JpaRepository<Urls, Long> {
    Urls findByKey(String key);
    Urls findByFullUrl(String fullUrl);
}