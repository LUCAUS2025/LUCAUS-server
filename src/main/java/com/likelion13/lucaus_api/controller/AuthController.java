package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.auth.LoginRequestDto;
import com.likelion13.lucaus_api.dto.request.auth.SignUpRequestDto;
import com.likelion13.lucaus_api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ApiResponse<String> signup(@RequestBody SignUpRequestDto request) {
        String result = authService.signup(request);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequestDto request) {
        String result = authService.login(request);
        return ApiResponse.onSuccess(result);
    }


}
