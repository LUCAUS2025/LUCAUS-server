package com.likelion13.lucaus_api.service;

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
import com.likelion13.lucaus_api.domain.entity.LostItems.Category;


@Service
public class LostItemsService {

    @Autowired
    private LostItemsRepository lostItemsRepository;

    @Transactional(readOnly = true)
    @Cacheable(
            value = "lost_10min",
            key = "#category + '-' + #date + '-' + #page + '-' + #size",
            condition = "#category != null && #date != null && #page > 0 && #size > 0"
    )
    public Page<LostItemsResponseDto> getLostItems(String category, String date, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);

        LocalDate parsedDate = LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        LocalDateTime startOfDay = parsedDate.atStartOfDay();
        LocalDateTime endOfDay = parsedDate.atTime(23, 59, 59, 999999999);

        Category categoryEnum;
        try {
            categoryEnum = Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid category: " + category);
        }

        Page<LostItems> lostItemsPage = lostItemsRepository.findByCategoryAndUpdatedDateTimeBetween(categoryEnum, startOfDay, endOfDay, pageable);

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
