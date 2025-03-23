package com.likelion13.lucaus_api.domain.repository;


import com.likelion13.lucaus_api.domain.entity.DetailedNotices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetailedNoticesRepository extends JpaRepository<DetailedNotices, Long> {
    Page<DetailedNotices> findAll(Pageable pageable);
}
