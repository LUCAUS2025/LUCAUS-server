package com.likelion13.lucaus_api.domain.entity;

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
    private LocalDateTime updatedDateTime;
    private String place;
    private String name;
    private String photoUrl;
    @Enumerated(EnumType.STRING)
    private Category category;
    private boolean ownerFound;
    public enum Category {
        COSMETICS, ELECTRONICS, CLOTHING, WALLET_CARD, OTHERS
    }
}
