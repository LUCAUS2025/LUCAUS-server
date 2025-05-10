package com.likelion13.lucaus_api.domain.entity.booth;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BoothCategoryMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private BoothCategory boothCategory;

    @ManyToOne
    @JoinColumn
    private Booth booth;
}
