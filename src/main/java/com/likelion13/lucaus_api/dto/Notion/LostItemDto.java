package com.likelion13.lucaus_api.dto.Notion;


import com.likelion13.lucaus_api.domain.entity.lostItems.LostItems;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LostItemDto {
    private LocalDateTime updatedDateTime;
    private String notionId;
    private String place;
    private String name;
    private String photoUrl;
    private String notionPhotoUrl;
    private String category;
    private boolean ownerFound;

    public LostItems.Category getCategoryEnum() {
        try {
            return LostItems.Category.valueOf(category.toUpperCase());
        } catch (IllegalArgumentException e) {
            return LostItems.Category.OTHERS;
        }
    }

    @Override
    public String toString() {
        return "LostItemDto{" +
                "updatedDateTime=" + updatedDateTime +
                ", notionId=" + notionId +
                ", place='" + place + '\'' +
                ", name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", category='" + category + '\'' +
                ", ownerFound=" + ownerFound +
                '}';
    }

}
