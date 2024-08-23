package de.telran.urlshortener.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "short_url")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String shortKey;

    @Column(nullable = false)
    private String fullUrl;

    @Column(nullable = false)
    private Long clickCount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
