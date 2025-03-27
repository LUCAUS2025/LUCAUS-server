package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.dto.response.LostItemsResponseDto;
import com.likelion13.lucaus_api.service.LostItemsService;
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
@Tag(name = "LostItems", description = "Operations related to LostItems")
public class LostItemsController {

    @Autowired
    private LostItemsService lostItemsService;

    @GetMapping
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
            @RequestParam @NotNull String category,
            @RequestParam @NotNull String date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<LostItemsResponseDto> responseDtoPage = lostItemsService.getLostItems(category, date, page, size);
            return ApiResponse.onSuccess(responseDtoPage);
        } catch (GeneralHandler e) {
            return ApiResponse.onFailure(e.getCode().toString(), e.getMessage(), null);
        }

    }
}

