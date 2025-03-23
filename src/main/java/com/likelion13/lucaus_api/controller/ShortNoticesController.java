package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.dto.response.ShortNoticesResponseDto;
import com.likelion13.lucaus_api.service.ShortNoticesService;
import com.likelion13.lucaus_api.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/short-notices")
public class ShortNoticesController {

    @Autowired
    private ShortNoticesService shortNoticesService;

    @GetMapping
    public ApiResponse<List<ShortNoticesResponseDto>> getVisibleShortNotices() {
        try {
            List<ShortNoticesResponseDto> responseDtoList = shortNoticesService.getVisibleShortNotices();

            return ApiResponse.onSuccess(responseDtoList);
        } catch (Exception e) {
            return ApiResponse.onFailure("500", "Internal Server Error", null);
        }
    }
}
