package de.telran.urlshortener.dto.urlDto;

import lombok.*;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrlRequest {
    private String fullUrl;
    private String email;
}