package com.likelion13.lucaus_api.dto.request.stamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStampRequestDto {
    private Integer type; // 도장판 종류
    private Integer stampBoothId; // 도장찍을 부스id
    private String pw; // 해당 부스 비밀번호
}
