package com.likelion13.lucaus_api.service.auth;

import com.likelion13.lucaus_api.dto.request.auth.LoginRequestDto;
import com.likelion13.lucaus_api.dto.request.auth.SignUpRequestDto;

public interface AuthService {
    String signup(SignUpRequestDto request);

    String login(LoginRequestDto request);
}
