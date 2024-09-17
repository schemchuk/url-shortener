package de.telran.urlshortener.entity;

import de.telran.urlshortener.entity.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @PrePersist
    public void prePersist() {
        if (this.startDate == null) {
            this.startDate = LocalDateTime.now();
        }
        if (this.endDate == null) {
            switch (this.subscriptionType) {
                case TRIAL:
                    this.endDate = this.startDate.plusDays(30);
                    break;
                case PAID:
                    this.endDate = this.startDate.plusDays(365);
                    break;
                case ADMIN:
                    this.endDate = null;
                    break;
            }
        }
    }
}
