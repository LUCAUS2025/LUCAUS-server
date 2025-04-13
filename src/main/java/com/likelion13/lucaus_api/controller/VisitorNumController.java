package com.likelion13.lucaus_api.controller;


import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.BoothReviewRequestDto;
import com.likelion13.lucaus_api.dto.request.VisitorNumRequestDto;
import com.likelion13.lucaus_api.dto.response.visitor.VisitorNumResponseDto;
import com.likelion13.lucaus_api.service.visitor.VisitorNumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitor-num")
@RequiredArgsConstructor
@Tag(name = "방문자 수 관련", description = "방문자 수 측정 관련 API입니다.")
public class VisitorNumController {
    private final VisitorNumService visitorNumService;

    @GetMapping("")
    @Operation(summary = "방문자 수 가져오기", description = "가장 마지막으로 등록된 방문자 수 가져오기 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<VisitorNumResponseDto> getNewestVisitorNum(){
        VisitorNumResponseDto result = visitorNumService.getNewestVisitorNum();
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("")
    @Operation(summary = "방문자 수 등록하기", description = "방문자 수를 기록하는 API입니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "성공적 조회",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "VISITOR400", description = "방문자 수는 필수값 입니다.(Integer)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiResponse.class))),
    })
    public ApiResponse<String> postNewestVisitorNum(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "방문자 수를 Integer로 전달",
                    required = true,
                    content = @Content(schema = @Schema(implementation = VisitorNumRequestDto.class))
            ) @RequestBody VisitorNumRequestDto visitorNumRequestDto){
        String result = visitorNumService.postNewestVisitorNum(visitorNumRequestDto);
        return ApiResponse.onSuccess(result);
    }
}
