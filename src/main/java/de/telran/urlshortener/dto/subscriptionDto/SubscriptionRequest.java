package de.telran.urlshortener.dto.subscriptionDto;

import de.telran.urlshortener.enums.SubscriptionType; // Импорт из правильного пакета
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionRequest {
    private SubscriptionType subscriptionType; // Используем enum
}


