package com.likelion13.lucaus_api.domain.repository.booth;

import com.likelion13.lucaus_api.domain.entity.booth.OpDateBooth;
import com.likelion13.lucaus_api.dto.response.booth.BoothListByDateResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(exported = false)
public interface OpDateBoothRepository extends JpaRepository<OpDateBooth, Long> {

    // 부스 정보 조회 (카테고리 제외)
    @Query(value = """
    SELECT o.day_booth_num AS dayBoothNum,
           b.id AS boothId,
           b.name AS name,
           b.info AS info
    FROM op_date_booth o
    JOIN booth b ON o.booth_id = b.id
    WHERE o.op_date = :opDate
    GROUP BY o.day_booth_num, b.id, b.name, b.info
    ORDER BY o.day_booth_num
    """, nativeQuery = true)
    List<Object[]> findBoothListByOpDate(@Param("opDate") Integer opDate);

    // 카테고리 정보 조회 (부스 ID 기준)
    @Query(value = """
    SELECT bcm.booth_id, GROUP_CONCAT(c.category) AS categories
    FROM booth_category_mapping bcm
    JOIN booth_category c ON bcm.booth_category_id = c.id
    WHERE bcm.booth_id IN :boothIds
    GROUP BY bcm.booth_id
    """, nativeQuery = true)
    List<Object[]> findCategoriesByBoothIds(@Param("boothIds") List<Long> boothIds);

    // 완전 추천해요 개수 가져오기
    @Query(value = """
    select brm.booth_id, brm.like_num
    from booth_review_mapping brm
    join booth_review r on brm.booth_review_id = r.id
    where brm.booth_id IN :boothIds and r.booth_review_tag = 'RECOMMEND'
    """, nativeQuery = true)
    List<Object[]> findRecommendByBoothIds(@Param("boothIds") List<Long> boothIds);
}