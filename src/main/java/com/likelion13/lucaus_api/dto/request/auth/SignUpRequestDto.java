package com.likelion13.lucaus_api.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String id;
    private String pw;
    private String name;
    private String studentId;
}
