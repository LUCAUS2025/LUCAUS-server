package com.likelion13.lucaus_api.service.booth;

import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;

import java.util.List;

public interface BoothOpDateService {
    List<BoothListByDateResponseDto> getBoothListByDate(Integer opDate);
}
