package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.dto.response.DetailedNoticesResponseDto;
import com.likelion13.lucaus_api.service.DetailedNoticesService;
import com.likelion13.lucaus_api.common.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
public class DetailedNoticesController {

    private final DetailedNoticesService detailedNoticesService;

    @Autowired
    public DetailedNoticesController(DetailedNoticesService detailedNoticesService) {
        this.detailedNoticesService = detailedNoticesService;
    }

    @GetMapping
    public ApiResponse<Page<DetailedNoticesResponseDto>> getNotices(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<DetailedNoticesResponseDto> noticesPage = detailedNoticesService.getNotices(page, size);

        return ApiResponse.onSuccess(noticesPage);
    }
}
