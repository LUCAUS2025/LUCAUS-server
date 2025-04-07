package com.likelion13.lucaus_api.domain.repository.booth;

import com.likelion13.lucaus_api.domain.entity.booth.Booth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothRepository extends JpaRepository<Booth, Long> {
}
