package com.likelion13.lucaus_api.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class UserInfoResponseDto {
    private String name; // 이름

    private String studentId; // 학번

}
