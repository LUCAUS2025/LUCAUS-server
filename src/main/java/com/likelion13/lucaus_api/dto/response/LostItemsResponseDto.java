package com.likelion13.lucaus_api.dto.response;

import com.likelion13.lucaus_api.domain.entity.LostItems;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LostItemsResponseDto {
    private Long id;
    private String updatedDateTime;
    private String place;
    private String name;
    private String photoUrl;
    private LostItems.Category category;
    private boolean ownerFound;
}
