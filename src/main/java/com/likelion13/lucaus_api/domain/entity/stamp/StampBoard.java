package com.likelion13.lucaus_api.domain.entity.stamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "stampBoard")
    private Set<StampBoardBoothMapping> stampBoardBoothMappings;

    public void getReward(Integer degree){
        switch (degree) {
            case 1:
                this.firstReward = true;
                break;
            case 2:
                this.secondReward = true;
                break;
            case 3:
                this.thirdReward = true;
                break;
            default:
                throw new IllegalArgumentException("존재하지 않는 수령 차수입니다.");
        }
    }
}
