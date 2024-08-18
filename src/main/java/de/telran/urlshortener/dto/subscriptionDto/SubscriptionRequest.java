package de.telran.urlshortener.dto.subscriptionDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private SubscriptionType subscriptionType;

    public enum SubscriptionType {
        TRIAL, PAID
    }
}

