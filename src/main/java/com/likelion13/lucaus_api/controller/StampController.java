package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;
import com.likelion13.lucaus_api.dto.request.stamp.AddStampRequestDto;
import com.likelion13.lucaus_api.dto.request.stamp.GetRewardRequestDto;
import com.likelion13.lucaus_api.dto.response.stamp.StampBoardInfoResponseDto;
import com.likelion13.lucaus_api.service.stamp.AddStampService;
import com.likelion13.lucaus_api.service.stamp.GetRewardService;
import com.likelion13.lucaus_api.service.stamp.StampInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stamp")
@RequiredArgsConstructor
@Tag(name = "도장판", description = "도장판 관련 API입니다.")
public class StampController {
    private final StampInfoService stampInfoService;

    private final AddStampService addStampService;

    private final GetRewardService getRewardService;

    // 유저별 도장판 조회
    @GetMapping
    @Operation(summary = "유저별 도장판 리스트", description = "유저별 도장판 리스트 조회 API입니다.(jwt필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<List<StampBoardInfoResponseDto>> getUserStampInfo(@AuthenticationPrincipal UserDetails userDetails) {
        String userId = userDetails.getUsername();
        List<StampBoardInfoResponseDto> result = stampInfoService.getStampBoards(userId);
        return ApiResponse.onSuccess(result);
    }

    // 부스도장찍기
    @PostMapping("/stamp-booth")
    @Operation(summary = "부스 도장 찍기", description = "도장판에 부스 도장 찍는 API입니다.(jwt필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PW400", description = "잘못된 비밀번호",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "BOOTH404", description = "부스 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "STAMP400", description = "중복 도장",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<String> addStamp(@AuthenticationPrincipal UserDetails userDetails,
                                        @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                description = "type은 1, 2 중 하나 / stampBoothId는 1~9 중 하나 / pw는 부스 pw",
                                                required = true,
                                                content = @Content(schema = @Schema(implementation = AddStampRequestDto.class))
                                        )@RequestBody AddStampRequestDto request ) {
        String userId = userDetails.getUsername();
        String result = addStampService.addStamp(userId, request.getType(), request.getStampBoothId(), request.getPw());
        return ApiResponse.onSuccess(result);
    }

    // 상품도장찍기
    @PostMapping("/reward")
    @Operation(summary = "보상 수령", description = "총학 보상 수령 확인 API입니다.(jwt필요)")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PW400", description = "잘못된 비밀번호",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REWARD4001", description = "도장 개수 불충분",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REWARD4002", description = "잘못된 차수",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "REWARD4003", description = "중복 수령",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<String> getReward(@AuthenticationPrincipal UserDetails userDetails,
                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                 description = "type은 1, 2 중 하나 / degree는 1~3 중 하나 / pw는 차수별 pw",
                                                 required = true,
                                                 content = @Content(schema = @Schema(implementation = GetRewardRequestDto.class))
                                         )@RequestBody GetRewardRequestDto request) {
        String userId = userDetails.getUsername();
        String result = getRewardService.getReward(userId, request.getType(), request.getDegree(), request.getPw());
        return ApiResponse.onSuccess(result);
    }

}
