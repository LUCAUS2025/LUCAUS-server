package com.likelion13.lucaus_api.service;

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
            value = "detail_5min",  // 캐시 이름
            key = "#page + '-' + #size",  // page와 size를 조합하여 캐시 키 생성
            condition = "#page > 0 && #size > 0"  // 페이지와 사이즈가 0보다 큰 경우에만 캐시 적용 (선택 사항)
    )
    public Page<DetailedNoticesResponseDto> getNotices(int page, int size) {
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
