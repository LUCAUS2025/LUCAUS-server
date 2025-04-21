package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.stamp.AddStampRequestDto;
import com.likelion13.lucaus_api.dto.request.stamp.GetRewardRequestDto;
import com.likelion13.lucaus_api.dto.response.stamp.StampBoardInfoResponseDto;
import com.likelion13.lucaus_api.service.stamp.AddStampService;
import com.likelion13.lucaus_api.service.stamp.GetRewardService;
import com.likelion13.lucaus_api.service.stamp.StampInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stamp")
@RequiredArgsConstructor
public class StampController {
    private final StampInfoService stampInfoService;

    private final AddStampService addStampService;

    private final GetRewardService getRewardService;

    // 유저별 도장판 조회
    @GetMapping
    public ApiResponse<List<StampBoardInfoResponseDto>> getUserStampInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<StampBoardInfoResponseDto> result = stampInfoService.getStampBoards(userId);
        return ApiResponse.onSuccess(result);
    }

    // 부스도장찍기
    @PostMapping("/stamp-booth")
    public ApiResponse<String> addStamp(@AuthenticationPrincipal UserDetails userDetails,
                                        @RequestBody AddStampRequestDto request ) {
        String userId = userDetails.getUsername();
        String result = addStampService.addStamp(userId, request.getType(), request.getStampBoothId(), request.getPw());
        return ApiResponse.onSuccess(result);
    }

    // 상품도장찍기
    @PostMapping("/reward")
    public ApiResponse<String> getReward(@AuthenticationPrincipal UserDetails userDetails,
                                         @RequestBody GetRewardRequestDto request) {
        String userId = userDetails.getUsername();
        String result = getRewardService.getReward(userId, request.getType(), request.getDegree(), request.getPw());
        return ApiResponse.onSuccess(result);
    }

}
