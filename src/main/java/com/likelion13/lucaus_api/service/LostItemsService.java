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
            throw new GeneralHandler(ErrorCode.INVALID_EMPTY);
        }

        if (!"TOTAL".equalsIgnoreCase(category) && !isValidCategory(category)) {
            throw new GeneralHandler(ErrorCode.INVALID_CATEGORY);
        }

        if (date == null || date.isEmpty()) {
            throw new GeneralHandler(ErrorCode.INVALID_EMPTY);
        }

        try {
            LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            throw new GeneralHandler(ErrorCode.INVALID_DATE);
        }

        if (page <= 0 || size <= 0) {
            throw new GeneralHandler(ErrorCode.INVALID_PAGE_SIZE);
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
