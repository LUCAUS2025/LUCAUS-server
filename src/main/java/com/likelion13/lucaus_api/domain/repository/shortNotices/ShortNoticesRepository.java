package com.likelion13.lucaus_api.domain.repository.shortNotices;
import com.likelion13.lucaus_api.domain.entity.shortNotices.ShortNotices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface ShortNoticesRepository extends JpaRepository<ShortNotices, Long> {
    List<ShortNotices> findByIsVisibleTrue();
    Optional<ShortNotices> findByNotionId(String notionId);
}
