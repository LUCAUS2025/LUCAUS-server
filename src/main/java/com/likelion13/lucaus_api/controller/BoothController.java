package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;
import com.likelion13.lucaus_api.service.booth.BoothDetailService;
import com.likelion13.lucaus_api.service.booth.BoothReviewService;
import com.likelion13.lucaus_api.service.booth.BoothOpDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/booth")
@RequiredArgsConstructor
@Tag(name = "거리문화제 부스", description = "거리문화제 부스 관련 API입니다.")
public class BoothController {

    private final BoothOpDateService boothOpDateService;

    private final BoothDetailService boothDetailService;

    private final BoothReviewService boothReviewService;

    @GetMapping("/{opDate}")
    @Operation(summary = "일자별 부스 리스트", description = "일자별 전체 부스 리스트 조회 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OPDATE400", description = "유효한 opDate(19~23) 아님",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOTH404", description = "조건 맞는 부스 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<List<BoothListByDateResponseDto>> getBoothListByDate(
            @Parameter(description = "운영일자 19~23 중 하나의 Integer", required = true) @PathVariable @NotNull Integer opDate){
        List<BoothListByDateResponseDto> result = boothOpDateService.getBoothListByDate(opDate);
        return ApiResponse.onSuccess(result);
    }



    @GetMapping("/{opDate}/{dayBoothNum}")
    @Operation(summary = "개별 부스 상세보기", description = "개별 부스 상세보기 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OPDATE400", description = "유효한 opDate(19~23) 아님",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOTH404", description = "조건 맞는 부스 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<List<BoothDetailResponseDto>> getBoothDetail(
            @Parameter(description = "운영일자 19~23 중 하나의 Integer", required = true) @PathVariable @NotNull Integer opDate,
            @Parameter(description = "일자별 부스 번호입니다.(부스 고유 아이디X)", required = true) @PathVariable @NotNull Integer dayBoothNum)
    {
        List<BoothDetailResponseDto> result = boothDetailService.getBoothDetailByOpDateAndDayBoothNum(opDate, dayBoothNum);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/review/{boothId}")
    @Operation(summary = "부스 리뷰 작성", description = "개별 부스에 리뷰 작성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW4001", description = "부스 운영시간 아님",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW4002", description = "잘못된 리뷰 태그",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOTH404", description = "조건 맞는 부스 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<String> postBoothReview(
            @Parameter(description = "부스 고유 아이디(일자별 부스 번호X)", required = true) @PathVariable @NotNull Long boothId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RECOMMEND, FUN, BENEFICIAL, DELICIOUS 의 조합을 리스트로 구성",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoothReviewRequestDto.class))
            ) @RequestBody BoothReviewRequestDto reviewRequest){
        String result = boothReviewService.postBoothReview(boothId, reviewRequest);
        return ApiResponse.onSuccess(result);
    }
}
