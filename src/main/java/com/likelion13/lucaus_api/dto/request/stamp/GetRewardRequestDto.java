package com.likelion13.lucaus_api.dto.request.stamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetRewardRequestDto {
    private Integer type; // 도장판 종류
    private Integer degree; // 상품 수령 차수
    private String pw; // 해당 차수 수령 비밀번호
}
