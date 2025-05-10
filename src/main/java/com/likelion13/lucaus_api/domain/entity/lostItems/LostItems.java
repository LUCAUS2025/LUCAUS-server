package com.likelion13.lucaus_api.domain.entity.lostItems;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class LostItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String notionId;
    private LocalDateTime updatedDateTime;
    private String place;
    private String name;
    private String photoUrl;
    private String notionPhotoUrl;
    @Enumerated(EnumType.STRING)
    private Category category;
    private boolean ownerFound;
    public enum Category {
        DAILY_NECESSITIES, ELECTRONICS, CLOTHING, WALLET_CARD, OTHERS
    }
    public void changePlace(String place) {
        this.place = place;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changePhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void changeNotionPhotoUrl(String notionPhotoUrl) {
        this.notionPhotoUrl = notionPhotoUrl;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changeOwnerFound(boolean ownerFound) {
        this.ownerFound = ownerFound;
    }
}
