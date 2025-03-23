package com.likelion13.lucaus_api.domain.repository;
import com.likelion13.lucaus_api.domain.entity.ShortNotices;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortNoticesRepository extends JpaRepository<ShortNotices, Long> {
    List<ShortNotices> findByIsVisibleTrue();
}
