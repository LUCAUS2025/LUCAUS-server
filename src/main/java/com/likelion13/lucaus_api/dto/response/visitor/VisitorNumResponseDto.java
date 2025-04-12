package com.likelion13.lucaus_api.dto.response.visitor;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VisitorNumResponseDto {
    private LocalDateTime updatedAt;
    private Integer visitorNum;
}
