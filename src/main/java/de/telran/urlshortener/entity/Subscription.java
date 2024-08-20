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

    @OneToOne(mappedBy = "subscription")
    private User user;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private SubscriptionType subscriptionType;

    @PostPersist
    @PostLoad
    public void updateDates() {
        this.startDate = LocalDateTime.now();
        if (subscriptionType == SubscriptionType.TRIAL) {
            this.endDate = this.startDate.plusMonths(1);
        } else if (subscriptionType == SubscriptionType.PAID) {
            this.endDate = this.startDate.plusYears(1);
        }
    }
}









