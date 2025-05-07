package com.likelion13.lucaus_api.dto.response;

import com.likelion13.lucaus_api.domain.entity.lostItems.LostItems;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LostItemsResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String updatedDateTime;
    private String place;
    private String name;
    private String photoUrl;
    private LostItems.Category category;
    private boolean ownerFound;
}
