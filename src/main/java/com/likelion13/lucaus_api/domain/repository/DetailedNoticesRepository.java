package com.likelion13.lucaus_api.domain.repository;


import com.likelion13.lucaus_api.domain.entity.DetailedNotices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DetailedNoticesRepository extends JpaRepository<DetailedNotices, Long> {
    Page<DetailedNotices> findAll(Pageable pageable);
    boolean existsByTitle(String title);
    Optional<DetailedNotices> findByNotionId(String notionId);
}
