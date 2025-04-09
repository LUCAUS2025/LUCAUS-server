package com.likelion13.lucaus_api.domain.entity.foodTruck;

import com.likelion13.lucaus_api.domain.entity.booth.Booth;
import com.likelion13.lucaus_api.domain.entity.booth.BoothCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FoodTruckMenuMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private FoodTruckMenu foodTruckMenu;

    @ManyToOne
    @JoinColumn
    private FoodTruck foodTruck;
}
