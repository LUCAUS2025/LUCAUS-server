package com.likelion13.lucaus_api.service.detailedNotices;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.detailedNotices.DetailedNotices;
import com.likelion13.lucaus_api.domain.repository.detailedNotices.DetailedNoticesRepository;
import com.likelion13.lucaus_api.dto.response.DetailedNoticesResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class DetailedNoticesService {

    private final DetailedNoticesRepository detailedNoticesRepository;

    @Autowired
    public DetailedNoticesService(DetailedNoticesRepository detailedNoticesRepository) {
        this.detailedNoticesRepository = detailedNoticesRepository;
    }

    @Cacheable(
            value = "notice_list_10min",
            key = "#page + '-' + #size",
            condition = "#page > 0 && #size > 0"
    )
    public Page<DetailedNoticesResponseDto> getNotices(int page, int size) {

        if (page <= 0 || size <= 0) {
            throw new GeneralHandler(ErrorCode.INVALID_PAGE_SIZE);
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "uploadDateTime"));
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

    @Cacheable(value = "notice_detail_10min", key = "#id")
    public DetailedNoticesResponseDto getNoticeById(Long id) {
        DetailedNotices notice = detailedNoticesRepository.findById(id)
                .orElseThrow(() -> new GeneralHandler(ErrorCode.NOTICE_NOT_FOUND));

        DetailedNoticesResponseDto dto = new DetailedNoticesResponseDto();
        dto.setId(notice.getId());
        dto.setCategory(notice.getCategory());
        dto.setTitle(notice.getTitle());
        dto.setContent(notice.getContent());
        dto.setPhotoUrl(notice.getPhotoUrl());
        dto.setUploadDateTime(notice.getUploadDateTime());

        return dto;
    }

}
