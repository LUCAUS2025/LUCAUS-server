package com.likelion13.lucaus_api.domain.entity.Booth;

import com.likelion13.lucaus_api.enums.BoothCategoryEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoothCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BoothCategoryEnum category; // 부스카테고리

    @OneToMany(mappedBy = "booth_category")
    private List<BoothCategoryMapping> boothCategoryMapping;
}
