package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.dto.response.LostItemsResponseDto;
import com.likelion13.lucaus_api.service.LostItemsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.likelion13.lucaus_api.common.response.ApiResponse;

@RestController
@RequestMapping("/api/lost-items")
@Tag(name = "분실물", description = "")
public class LostItemsController {

    @Autowired
    private LostItemsService lostItemsService;

    @GetMapping
    @Operation(summary = "분실물", description = "분실물을 조회하는 API입니다. ")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 - 잘못된 카테고리나 날짜",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 - 잘못된 페이지 번호나 크기",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<Page<LostItemsResponseDto>> getLostItems(
            @Parameter(description = "카테고리 (COSMETICS, ELECTRONICS, CLOTHING, WALLET_CARD, OTHERS,TOTAL)", required = true) @RequestParam @NotNull String category,
            @Parameter(description = "날짜 (형식: YYYY-MM-DD)", required = true) @RequestParam @NotNull String date,
            @Parameter(description = "페이지 (1보다 커야합니다)", required = true) @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "사이즈 : 페이지 안에 들어있는 객체의 수 (1보다 커야합니다)", required = true) @RequestParam(defaultValue = "10") int size) {

        try {
            Page<LostItemsResponseDto> responseDtoPage = lostItemsService.getLostItems(category, date, page, size);
            return ApiResponse.onSuccess(responseDtoPage);
        } catch (GeneralHandler e) {
            return ApiResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
        }

    }
}

