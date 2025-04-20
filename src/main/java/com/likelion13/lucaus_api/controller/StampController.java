package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;
import com.likelion13.lucaus_api.dto.response.stamp.StampBoardInfoResponseDto;
import com.likelion13.lucaus_api.service.stamp.StampService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stamp")
@RequiredArgsConstructor
public class StampController {
    private final StampService stampService;

    @GetMapping
    public ApiResponse<List<StampBoardInfoResponseDto>> getUserStampInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<StampBoardInfoResponseDto> result = stampService.getStampBoards(userId);
        return ApiResponse.onSuccess(result);
    }
}
