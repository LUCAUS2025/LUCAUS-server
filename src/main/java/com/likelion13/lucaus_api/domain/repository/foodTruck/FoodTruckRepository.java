package com.likelion13.lucaus_api.domain.repository.foodTruck;

import com.likelion13.lucaus_api.domain.entity.foodTruck.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FoodTruckRepository extends JpaRepository<FoodTruck, Long> {
}
