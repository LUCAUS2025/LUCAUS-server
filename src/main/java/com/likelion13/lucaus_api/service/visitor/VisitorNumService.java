package com.likelion13.lucaus_api.service.visitor;

import com.likelion13.lucaus_api.dto.request.VisitorNumRequestDto;
import com.likelion13.lucaus_api.dto.response.visitor.VisitorNumResponseDto;

public interface VisitorNumService {
    VisitorNumResponseDto getNewestVisitorNum();

    String postNewestVisitorNum(VisitorNumRequestDto visitorNumRequestDto);
}
