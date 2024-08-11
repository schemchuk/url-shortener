package de.telran.urlshortener.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ShortUrlResponse {
    private String key;
}
