package de.telran.urlshortener.dto.userDto;

import de.telran.urlshortener.dto.RoleDto.RoleResponse;
import de.telran.urlshortener.dto.urlDto.ShortUrlResponse;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String userName;
    private String email;
    private Set<RoleResponse> roles;
    private Set<ShortUrlResponse> shortUrls;
}




