package com.likelion13.lucaus_api.domain.repository.stamp;

import com.likelion13.lucaus_api.domain.entity.stamp.StampBooth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface StampBoothRepository extends JpaRepository<StampBooth, Long> {
}
