package com.likelion13.lucaus_api.domain.entity.stemp;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserStampBoardMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean firstReward; // 1차 수령

    private Boolean secondReward; // 2차 수령

    private Boolean thirdReward; // 3차 수령

    @ManyToOne
    @JoinColumn
    private StampBoard stampBoard;

    @ManyToOne
    @JoinColumn
    private User user;
}
