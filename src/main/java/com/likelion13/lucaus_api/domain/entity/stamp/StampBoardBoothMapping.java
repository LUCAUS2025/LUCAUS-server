package com.likelion13.lucaus_api.domain.entity.stamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private StampBooth stampBooth;

    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private StampBoard stampBoard;

    public void addStamp(){
        this.isClear = true;
    }
}
