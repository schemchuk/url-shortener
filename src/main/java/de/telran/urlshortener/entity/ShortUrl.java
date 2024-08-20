package de.telran.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "short_url")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shortKey;

    @Column(nullable = false)
    private String fullUrl;

    @Column(nullable = false)
    private Long clickCount;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}


