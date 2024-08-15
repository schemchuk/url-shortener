package de.telran.urlshortener.dto.userDto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    private String userName;
    private String password;
    private String email;
}
