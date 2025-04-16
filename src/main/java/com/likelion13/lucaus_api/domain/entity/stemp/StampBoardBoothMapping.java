package com.likelion13.lucaus_api.domain.entity.stemp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StampBoardBoothMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean isClear; // 부스 도장 획득 여부

    @ManyToOne
    @JoinColumn
    private StampBooth stampBooth;

    @ManyToOne
    @JoinColumn
    private StampBoard stampBoard;
}
