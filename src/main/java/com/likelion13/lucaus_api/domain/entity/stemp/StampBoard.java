package com.likelion13.lucaus_api.domain.entity.stemp;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StampBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 도장판 Id

    private Integer type; // 도장판 타입 1이면 19-20 / 2이면 21

    private Boolean firstReward; // 1차 수령

    private Boolean secondReward; // 2차 수령

    private Boolean thirdReward; // 3차 수령

    @ManyToOne
    @JoinColumn
    private User user;

    @OneToMany(mappedBy = "stampBoard")
    private Set<StampBoardBoothMapping> stampBoardBoothMappings;
}
