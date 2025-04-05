package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;
import com.likelion13.lucaus_api.service.booth.BoothDetailService;
import com.likelion13.lucaus_api.service.booth.OpDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/booth")
@RequiredArgsConstructor
public class BoothController {

    private final OpDateService opDateService;

    @GetMapping("/{opDate}")
    public ApiResponse<List<BoothListByDateResponseDto>> getBoothListByDate(@PathVariable Integer opDate){
        List<BoothListByDateResponseDto> result = opDateService.getBoothListByDate(opDate);
        return ApiResponse.onSuccess(result);
    }

    private final BoothDetailService boothDetailService;

    @GetMapping("/{opDate}/{dayBoothNum}")
    public ApiResponse<List<BoothDetailResponseDto>> getBoothDetail(@PathVariable Integer opDate, @PathVariable Integer dayBoothNum){
        List<BoothDetailResponseDto> result = boothDetailService.getBoothDetailByOpDateAndDayBoothNum(opDate, dayBoothNum);
        return ApiResponse.onSuccess(result);
    }
}
