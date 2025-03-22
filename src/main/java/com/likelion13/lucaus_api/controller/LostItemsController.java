package com.likelion13.lucaus_api.controller;

import com.likelion13.lucaus_api.domain.entity.LostItems;
import com.likelion13.lucaus_api.dto.response.LostItemsResponseDto;
import com.likelion13.lucaus_api.service.LostItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import com.likelion13.lucaus_api.common.response.ApiResponse;

@RestController
@RequestMapping("/api/lost-items")
public class LostItemsController {

    @Autowired
    private LostItemsService lostItemsService;

    @GetMapping
    public ApiResponse<Page<LostItemsResponseDto>> getLostItems(
            @RequestParam String category,
            @RequestParam String date,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        try {
            Page<LostItems> lostItemsPage = lostItemsService.getLostItems(category, date, page, size);

            Page<LostItemsResponseDto> responseDtoPage = lostItemsPage.map(lostItem -> {
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

            return ApiResponse.onSuccess(responseDtoPage);

        } catch (Exception e) {
            return ApiResponse.onFailure("500", "Internal Server Error", null);
        }
    }
}


