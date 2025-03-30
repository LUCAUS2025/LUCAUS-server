package com.likelion13.lucaus_api.domain.entity.Booth;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "op_date_booth", indexes = @Index(name = "idx_op_date", columnList = "opDate"))
public class OpDateBooth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime opDate; // 운영일자

    private Integer opTimeStart; // 운영시작시간

    private Integer opTimeEnd; // 운영마감시간

    private String location; // 위치

    private Integer detailLocation; // 세부위치번호

    private Integer dayBoothNum; // 날짜별 부스 번호

    @ManyToOne
    @JoinColumn
    private Booth booth;
}
