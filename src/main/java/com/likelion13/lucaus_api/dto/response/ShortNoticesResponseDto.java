package com.likelion13.lucaus_api.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShortNoticesResponseDto {
    private Long id;
    private String info;
    private LocalDateTime uploadDateTime;

}
