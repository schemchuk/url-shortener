package de.telran.urlshortener.dto.subscriptionDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionResponse {
    private Long id;
    private String startDate;
    private String endDate;
    private String subscriptionType; // Используем String для типа подписки
}



