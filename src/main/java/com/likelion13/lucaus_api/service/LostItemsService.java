package com.likelion13.lucaus_api.service;

import com.likelion13.lucaus_api.common.exception.ErrorCode;
import com.likelion13.lucaus_api.common.exception.GeneralHandler;
import com.likelion13.lucaus_api.domain.entity.LostItems;
import com.likelion13.lucaus_api.domain.repository.LostItemsRepository;
import com.likelion13.lucaus_api.dto.response.LostItemsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.likelion13.lucaus_api.domain.entity.LostItems.Category;
@Service
public class LostItemsService {

    @Autowired
    private LostItemsRepository lostItemsRepository;
    private static final String[] VALID_CATEGORIES = {"TOTAL","COSMETICS", "ELECTRONICS", "CLOTHING", "WALLET_CARD", "OTHERS"};
    private boolean isValidCategory(String category) {
        if (category == null) {
            return false;
        }
        for (String validCategory : VALID_CATEGORIES) {
            if (validCategory.equalsIgnoreCase(category)) {
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    @Cacheable(
            value = "lost_10min",
            key = "#category + '-' + #date + '-' + #page + '-' + #size",
            condition = "#category != null && #date != null && #page > 0 && #size > 0"
    )
    public Page<LostItemsResponseDto> getLostItems(String category, String date, int page, int size) {

        if (category == null || category.isEmpty()) {
//            throw new GeneralHandler(ErrorCode._BAD_REQUEST, "카테고리는 필수 입력 값입니다.");
            throw new GeneralHandler(ErrorCode._BAD_REQUEST);
        }

        if (!"TOTAL".equalsIgnoreCase(category) && !isValidCategory(category)) {
//            throw new GeneralHandler(ErrorCode._BAD_REQUEST, "잘못된 카테고리입니다. (유효한 카테고리: TOTAL, COSMETICS, ELECTRONICS, CLOTHING, WALLET_CARD, OTHERS)");
            throw new GeneralHandler(ErrorCode._BAD_REQUEST);
        }

        if (date == null || date.isEmpty()) {
//            throw new GeneralHandler(ErrorCode._BAD_REQUEST, "날짜는 필수 입력 값입니다.");
            throw new GeneralHandler(ErrorCode._BAD_REQUEST);
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
//            throw new GeneralHandler(ErrorCode._BAD_REQUEST, "날짜 형식이 올바르지 않습니다. (형식: YYYY-MM-DD)");
            throw new GeneralHandler(ErrorCode._BAD_REQUEST);
        }

        if (page <= 0 || size <= 0) {
//            throw new GeneralHandler(ErrorCode._BAD_REQUEST, "페이지와 사이즈는 1 이상의 값이어야 합니다.");
            throw new GeneralHandler(ErrorCode._BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page - 1, size);

        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = parsedDate.atStartOfDay();
        LocalDateTime endOfDay = parsedDate.atTime(23, 59, 59, 999999999);

        Page<LostItems> lostItemsPage;
        if ("TOTAL".equalsIgnoreCase(category)) {
            lostItemsPage = lostItemsRepository.findByUpdatedDateTimeBetween(startOfDay, endOfDay, pageable);
        } else {
            Category categoryEnum;
            try {
                categoryEnum = Category.valueOf(category.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid category: " + category);
            }

            lostItemsPage = lostItemsRepository.findByCategoryAndUpdatedDateTimeBetween(categoryEnum, startOfDay, endOfDay, pageable);
        }

        return lostItemsPage.map(lostItem -> {
            LostItemsResponseDto responseDto = new LostItemsResponseDto();
            responseDto.setId(lostItem.getId());
            responseDto.setUpdatedDateTime(lostItem.getUpdatedDateTime().toString());
            responseDto.setPlace(lostItem.getPlace());
            responseDto.setName(lostItem.getName());
            responseDto.setPhotoUrl(lostItem.getPhotoUrl());
            responseDto.setCategory(lostItem.getCategory());
            responseDto.setOwnerFound(lostItem.isOwnerFound());
            return responseDto;
        });
    }
}
