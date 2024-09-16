package de.telran.urlshortener.dto.urldto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortUrlResponse {
    private Long id;
    private String shortKey;
    private String fullUrl;
    private Long clickCount;
}


