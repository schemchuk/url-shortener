package de.telran.urlshortener.dto.userDto;

import de.telran.urlshortener.dto.subscriptionDto.SubscriptionRequest;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String userName;
    private String email;
    private String password;
    private SubscriptionRequest subscription;
}
