package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.dto.response.DetailedNoticesResponseDto;
import com.likelion13.lucaus_api.service.DetailedNoticesService;
import com.likelion13.lucaus_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@Tag(name = "공지 사항", description = "리스트로 보여지는 공지 사항! 토스트와 다름 주의!")
public class DetailedNoticesController {

    private final DetailedNoticesService detailedNoticesService;

    @Autowired
    public DetailedNoticesController(DetailedNoticesService detailedNoticesService) {
        this.detailedNoticesService = detailedNoticesService;
    }

    @GetMapping
    @Operation(summary = "공지사항", description = "공지사항을 조회하는 API입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<Page<DetailedNoticesResponseDto>> getNotices(
            @Parameter(description = "페이지 (1보다 커야합니다)", required = true) @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "사이즈 : 페이지 안에 들어있는 객체의 수 (1보다 커야합니다)", required = true) @RequestParam(defaultValue = "10") int size) {

        Page<DetailedNoticesResponseDto> noticesPage = detailedNoticesService.getNotices(page, size);

        return ApiResponse.onSuccess(noticesPage);
    }
}
