package de.telran.urlshortener.dto.urlDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrlResponse {
    private String key;
    private String fullUrl;
}

