package com.likelion13.lucaus_api.domain.repository.foodTruck;

import com.likelion13.lucaus_api.domain.entity.foodTruck.FoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface FoodTruckDetailRepository extends JpaRepository<FoodTruck, Long> {
    @Query(value= """
    select o.day_food_truck_num as dayFoodTruckNum,
           ft.id as foodTruckId,
           ft.name as name,
           ft.cover as cover,
           o.location as location
    from op_date_food_truck o
    join food_truck ft on o.food_truck_id = ft.id
    where o.op_date = :opDate and o.day_food_truck_num = :dayFoodTruckNum
   """, nativeQuery = true)
    List<Object[]> findFoodTruckDetailByOpDateAndDayFoodTruckNum(@Param("opDate")Integer opDate, @Param("dayFoodTruckNum")Integer dayFoodTruckNum);

    // 푸드트럭 메뉴, 가격 조회
    @Query(value = """
    select ftm.menu_name, ftm.menu_price
    from food_truck_menu ftm
    where ftm.food_truck_id = :foodTruckId
""", nativeQuery = true)
    List<Object[]> findBoothMenu(@Param("foodTruckId")Long foodTruckId);

    // 푸드트럭 리뷰 조회
    @Query(value = """
    select ftr.food_truck_review_tag, ftrm.like_num
    from food_truck_review_mapping ftrm
    join food_truck_review ftr on ftrm.food_truck_review_id = ftr.id
    where ftrm.food_truck_id = :foodTruckId
""", nativeQuery = true)
    List<Object[]> findFoodTruckReviewsByFoodTruckId(@Param("foodTruckId")Long foodTruckId);

    // foodTruckId 기준 운영일자 리스트 조회
    @Query(value = """
    SELECT DISTINCT o.op_date
    FROM op_date_food_truck o
    WHERE o.food_truck_id = :foodTruckId
    ORDER BY o.op_date
""", nativeQuery = true)
    List<Integer> findOpDateListByFoodTruckId(@Param("foodTruckId") Long foodTruckId);
}
