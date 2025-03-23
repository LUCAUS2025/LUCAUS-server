package com.likelion13.lucaus_api.service;

import com.likelion13.lucaus_api.domain.entity.ShortNotices;
import com.likelion13.lucaus_api.domain.repository.ShortNoticesRepository;
import com.likelion13.lucaus_api.dto.response.ShortNoticesResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortNoticesService {

    @Autowired
    private ShortNoticesRepository shortNoticesRepository;

    @Cacheable(value = "short_1min", key = "'visibleShortNotices'")
    public List<ShortNoticesResponseDto> getVisibleShortNotices() {
        List<ShortNotices> shortNoticesList = shortNoticesRepository.findByIsVisibleTrue();

        return shortNoticesList.stream().map(shortNotice -> {
            ShortNoticesResponseDto responseDto = new ShortNoticesResponseDto();
            responseDto.setId(shortNotice.getId());
            responseDto.setInfo(shortNotice.getInfo());
            responseDto.setUploadDateTime(shortNotice.getUploadDateTime());
            return responseDto;
        }).collect(Collectors.toList());
    }
}
