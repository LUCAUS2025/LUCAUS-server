package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;
import com.likelion13.lucaus_api.service.booth.BoothDetailService;
import com.likelion13.lucaus_api.service.booth.BoothReviewService;
import com.likelion13.lucaus_api.service.booth.OpDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booth")
@RequiredArgsConstructor
public class BoothController {

    private final OpDateService opDateService;

    private final BoothDetailService boothDetailService;

    private final BoothReviewService boothReviewService;

    @GetMapping("/{opDate}")
    public ApiResponse<List<BoothListByDateResponseDto>> getBoothListByDate(@PathVariable Integer opDate){
        List<BoothListByDateResponseDto> result = opDateService.getBoothListByDate(opDate);
        return ApiResponse.onSuccess(result);
    }



    @GetMapping("/{opDate}/{dayBoothNum}")
    public ApiResponse<List<BoothDetailResponseDto>> getBoothDetail(@PathVariable Integer opDate, @PathVariable Integer dayBoothNum){
        List<BoothDetailResponseDto> result = boothDetailService.getBoothDetailByOpDateAndDayBoothNum(opDate, dayBoothNum);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/review/{boothId}")
    public ApiResponse<String> postBoothReview(@PathVariable Long boothId, @RequestBody BoothReviewRequestDto reviewRequest){
        String result = boothReviewService.postBoothReview(boothId, reviewRequest);
        return ApiResponse.onSuccess(result);
    }
}
