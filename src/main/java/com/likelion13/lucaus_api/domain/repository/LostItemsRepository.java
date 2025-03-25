package com.likelion13.lucaus_api.domain.repository;

import com.likelion13.lucaus_api.domain.entity.LostItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import com.likelion13.lucaus_api.domain.entity.LostItems.Category;

public interface LostItemsRepository extends JpaRepository<LostItems, Long> {

    Page<LostItems> findByCategoryAndUpdatedDateTimeBetween(
            Category category, LocalDateTime startOfDay, LocalDateTime endOfDay, Pageable pageable);

            Page<LostItems> findByUpdatedDateTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
