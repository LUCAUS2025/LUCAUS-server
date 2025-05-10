package com.likelion13.lucaus_api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VisitorNumRequestDto {
    private Integer visitorNum;
}
