package com.likelion13.lucaus_api.domain.entity.foodTruck;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodTruckMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String menuName; // 메뉴이름

    private Integer menuPrice;// 메뉴가격

    private Boolean isRepresent; // 대표메뉴여부

    @ManyToOne
    @JoinColumn
    private FoodTruck foodTruck;
}
