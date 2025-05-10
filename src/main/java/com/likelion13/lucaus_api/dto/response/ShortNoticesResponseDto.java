package com.likelion13.lucaus_api.dto.response;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ShortNoticesResponseDto implements Serializable  {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String info;
    private LocalDateTime uploadDateTime;

}
