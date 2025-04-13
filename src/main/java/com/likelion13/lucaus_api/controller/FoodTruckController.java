package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;
import com.likelion13.lucaus_api.dto.request.FoodTruckReviewRequestDto;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.FoodTruck.FoodTruckListByDateResponseDto;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckDetailService;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckOpDateService;
import com.likelion13.lucaus_api.service.foodTruck.FoodTruckReviewService;
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
@RequestMapping("/api/food-truck")
@RequiredArgsConstructor
@Tag(name = "푸드트럭", description = "푸ㅡ트럭 관련 API입니다.")
public class FoodTruckController {

    private final FoodTruckOpDateService foodTruckOpDateService;

    private final FoodTruckDetailService foodTruckDetailService;

    private final FoodTruckReviewService foodTruckReviewService;

    @GetMapping("/{opDate}")
    @Operation(summary = "일자별 푸드트럭 리스트", description = "일자별 전체 푸드트럭 리스트 조회 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OPDATE400", description = "유효한 opDate(19~23) 아님",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FOOD-TRUCK404", description = "조건 맞는 푸드트럭 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<List<FoodTruckListByDateResponseDto>> getFoodTruckListByDate(
            @Parameter(description = "운영일자 19~23 중 하나의 Integer", required = true) @PathVariable @NotNull Integer opDate){
        List<FoodTruckListByDateResponseDto> result = foodTruckOpDateService.getFoodTruckListByDate(opDate);
        return ApiResponse.onSuccess(result);
    }

    @GetMapping("/{opDate}/{dayFoodTruckNum}")
    @Operation(summary = "개별 푸드트럭 상세보기", description = "개별 푸드트럭 상세보기 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "OPDATE400", description = "유효한 opDate(19~23) 아님",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FOOD-TRUCK404", description = "조건 맞는 푸드트럭 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<List<FoodTruckDetailResponseDto>> getFoodTruckDetail(
            @Parameter(description = "운영일자 19~23 중 하나의 Integer", required = true) @PathVariable @NotNull Integer opDate,
            @Parameter(description = "일자별 푸드트럭 번호입니다.(푸드트럭 고유 아이디X)", required = true) @PathVariable @NotNull Integer dayFoodTruckNum){
        List<FoodTruckDetailResponseDto> result = foodTruckDetailService.getFoodTruckDetailByOpDateAndDayFoodTruckNum(opDate, dayFoodTruckNum);
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("/review/{foodTruckId}")
    @Operation(summary = "푸드트럭 리뷰 작성", description = "개별 푸드트럭에 리뷰 작성하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW4001", description = "푸드트럭 운영시간 아님",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REVIEW4002", description = "잘못된 리뷰 태그",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "FOOD-TRUCK404", description = "조건 맞는 푸드트럭 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<String> postFoodTruckReview(
            @Parameter(description = "부스 고유 아이디(일자별 부스 번호X)", required = true) @PathVariable @NotNull Long foodTruckId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RECOMMEND, DELICIOUS, MANY, FAST 의 조합을 리스트로 구성",
                    required = true,
                    content = @Content(schema = @Schema(implementation = FoodTruckReviewRequestDto.class))
            ) @RequestBody FoodTruckReviewRequestDto reviewRequest){
        String result = foodTruckReviewService.postFoodTruckReview(foodTruckId, reviewRequest);
        return ApiResponse.onSuccess(result);
    }
}
