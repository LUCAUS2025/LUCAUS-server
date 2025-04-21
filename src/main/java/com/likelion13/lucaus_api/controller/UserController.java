package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.response.user.UserInfoResponseDto;
import com.likelion13.lucaus_api.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<UserInfoResponseDto> getUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        UserInfoResponseDto result = userService.getCurrentUserInfo(userId);
        return ApiResponse.onSuccess(result);
    }
}
