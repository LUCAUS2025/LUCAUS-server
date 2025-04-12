package com.likelion13.lucaus_api.domain.repository.foodTruck;

import com.likelion13.lucaus_api.domain.entity.foodTruck.OpDateFoodTruck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface OpDateFoodTruckRepository extends JpaRepository<OpDateFoodTruck, Long> {

    // 푸드트럭 조회
    @Query(value = """
    select o.day_food_truck_num as dayFoodTruckNum,
       f.id as foodTruckId,
       f.name as name
    from op_date_food_truck o
    join food_truck f on o.food_truck_id = f.id
    where o.op_date = :opDate
    group by o.day_food_truck_num, f.id, f.name
    order by o.day_food_truck_num
    """, nativeQuery = true)
    List<Object[]> findFoodTruckListByOpDate(@Param("opDate") Integer opDate);

    // 푸드트럭 대표메뉴 조회
    @Query(value = """
    select ftm.food_truck_id, GROUP_CONCAT(ftm.menu_name) as menus
    from food_truck_menu ftm
    join food_truck f on ftm.food_truck_id = f.id
    where ftm.food_truck_id IN :foodTruckIds and ftm.is_represent
    group by ftm.food_truck_id
    """, nativeQuery = true)
    List<Object[]> findRepresentMenusByFoodTruckId(@Param("foodTruckIds") List<Long> foodTruckIds);
}
