package com.likelion13.lucaus_api.controller;


import com.likelion13.lucaus_api.common.response.ApiResponse;
import com.likelion13.lucaus_api.dto.request.VisitorNumRequestDto;
import com.likelion13.lucaus_api.dto.response.visitor.VisitorNumResponseDto;
import com.likelion13.lucaus_api.service.visitor.VisitorNumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/visitor-num")
@RequiredArgsConstructor
public class VisitorNumController {
    private final VisitorNumService visitorNumService;

    @GetMapping("")
    public ApiResponse<VisitorNumResponseDto> getNewestVisitorNum(){
        VisitorNumResponseDto result = visitorNumService.getNewestVisitorNum();
        return ApiResponse.onSuccess(result);
    }

    @PostMapping("")
    public ApiResponse<String> postNewestVisitorNum(@RequestBody VisitorNumRequestDto visitorNumRequestDto){
        String result = visitorNumService.postNewestVisitorNum(visitorNumRequestDto);
        return ApiResponse.onSuccess(result);
    }
}
