package de.telran.urlshortener.dto.urlDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShortUrlRequest {
    private String url;
    private String email;
}
