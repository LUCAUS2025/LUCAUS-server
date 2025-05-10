package com.likelion13.lucaus_api.domain.repository.detailedNotices;


import com.likelion13.lucaus_api.domain.entity.detailedNotices.DetailedNotices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface DetailedNoticesRepository extends JpaRepository<DetailedNotices, Long> {
    Page<DetailedNotices> findAll(Pageable pageable);
    boolean existsByTitle(String title);
    Optional<DetailedNotices> findByNotionId(String notionId);
}
