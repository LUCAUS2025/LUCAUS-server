package com.likelion13.lucaus_api.domain.repository.booth;


import com.likelion13.lucaus_api.domain.entity.booth.Booth;
import com.likelion13.lucaus_api.dto.response.booth.BoothDetailResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface BoothDetailRepository extends JpaRepository<Booth, Long> {
    @Query(value = """
    SELECT o.day_booth_num AS dayBoothNum,
           b.id AS boothId,
           b.name AS name,
           b.owner AS owner,
           b.info AS info,
           b.cover AS cover,
           o.location AS location
    FROM op_date_booth o
    JOIN booth b ON o.booth_id = b.id
    WHERE o.op_date = :opDate AND o.day_booth_num = :dayBoothNum
""", nativeQuery = true)
    List<Object[]> findBoothDetailByOpDateAndDayBoothNum(@Param("opDate")Integer opDate, @Param("dayBoothNum")Integer dayBoothNum);

    // 부스 카테고리 조회
    @Query(value = """
    SELECT c.category
    FROM booth_category_mapping bcm
    JOIN booth_category c ON bcm.booth_category_id = c.id
    WHERE bcm.booth_id = :boothId
    """, nativeQuery = true)
    List<String> findBoothCategoriesByBoothId(@Param("boothId") Long boothId);

    // 부스 리뷰 조회
    @Query(value = """
    SELECT br.booth_review_tag, brm.like_num
    FROM booth_review_mapping brm
    JOIN booth_review br ON brm.booth_review_id = br.id
    WHERE brm.booth_id = :boothId
    """, nativeQuery = true)
    List<Object[]> findBoothReviewsByBoothId(@Param("boothId") Long boothId);

    // boothId 기준 운영일자 리스트 조회
    @Query(value = """
    SELECT DISTINCT o.op_date
    FROM op_date_booth o
    WHERE o.booth_id = :boothId
    ORDER BY o.op_date
""", nativeQuery = true)
    List<Integer> findOpDateListByBoothId(@Param("boothId") Long boothId);
}
