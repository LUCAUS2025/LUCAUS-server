package com.likelion13.lucaus_api.service.booth;

import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;

import java.util.List;

public interface BoothDetailService {
    List<BoothDetailResponseDto> getBoothDetailByOpDateAndDayBoothNum(Integer opDate, Integer dayBoothNum);
}
