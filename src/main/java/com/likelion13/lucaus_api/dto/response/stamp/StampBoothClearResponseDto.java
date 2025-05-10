package com.likelion13.lucaus_api.dto.response.stamp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StampBoothClearResponseDto {
    private Long boothId; // 스탬프부스 id

    private Boolean isClear; // 수행여부
}
