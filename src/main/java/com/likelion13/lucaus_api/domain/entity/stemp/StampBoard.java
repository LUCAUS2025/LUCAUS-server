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

    @OneToMany(mappedBy = "stampBoard")
    private Set<UserStampBoardMapping> userStampBoardMappings;

    @OneToMany(mappedBy = "stampBoard")
    private Set<StampBoardBoothMapping> stampBoardBoothMappings;
}
