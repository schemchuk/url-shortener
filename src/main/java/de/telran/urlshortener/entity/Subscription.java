package de.telran.urlshortener.entity;

import de.telran.urlshortener.enums.SubscriptionType;
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
    private User user; // Связь с User

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
            if (this.subscriptionType == SubscriptionType.TRIAL) {
                this.endDate = this.startDate.plusMonths(1);
            } else if (this.subscriptionType == SubscriptionType.PAID) {
                this.endDate = this.startDate.plusYears(1);
            }
        }
    }
}










