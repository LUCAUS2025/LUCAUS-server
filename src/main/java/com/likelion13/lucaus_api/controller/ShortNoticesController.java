package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.dto.response.ShortNoticesResponseDto;
import com.likelion13.lucaus_api.service.ShortNoticesService;
import com.likelion13.lucaus_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/short-notices")
@Tag(name = "ShortNotices", description = "Operations related to ShortNotices")
public class ShortNoticesController {

    @Autowired
    private ShortNoticesService shortNoticesService;

    @GetMapping
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<List<ShortNoticesResponseDto>> getVisibleShortNotices() {
            List<ShortNoticesResponseDto> responseDtoList = shortNoticesService.getVisibleShortNotices();
            return ApiResponse.onSuccess(responseDtoList);
    }
}
