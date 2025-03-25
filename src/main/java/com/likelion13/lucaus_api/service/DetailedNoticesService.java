package com.likelion13.lucaus_api.service;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.DetailedNotices;
import com.likelion13.lucaus_api.domain.repository.DetailedNoticesRepository;
import com.likelion13.lucaus_api.dto.response.DetailedNoticesResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class DetailedNoticesService {

    private final DetailedNoticesRepository detailedNoticesRepository;

    @Autowired
    public DetailedNoticesService(DetailedNoticesRepository detailedNoticesRepository) {
        this.detailedNoticesRepository = detailedNoticesRepository;
    }

    @Cacheable(
            value = "detail_5min",
            key = "#page + '-' + #size",
            condition = "#page > 0 && #size > 0"
    )
    public Page<DetailedNoticesResponseDto> getNotices(int page, int size) {

        if (page <= 0 || size <= 0) {
//            throw new GeneralHandler(ErrorCode._BAD_REQUEST,"페이지와 사이즈는 1 이상의 값이어야 합니다.");
            throw new GeneralHandler(ErrorCode._BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DetailedNotices> noticesPage = detailedNoticesRepository.findAll(pageable);

        Page<DetailedNoticesResponseDto> dtoPage = noticesPage.map(notice -> {
            DetailedNoticesResponseDto dto = new DetailedNoticesResponseDto();
            dto.setId(notice.getId());
            dto.setCategory(notice.getCategory());
            dto.setTitle(notice.getTitle());
            dto.setContent(notice.getContent());
            dto.setPhotoUrl(notice.getPhotoUrl());
            dto.setUploadDateTime(notice.getUploadDateTime());
            return dto;
        });

        return dtoPage;
    }
}
