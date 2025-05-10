package com.likelion13.lucaus_api.domain.repository.booth;

import com.likelion13.lucaus_api.domain.entity.booth.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;


@RepositoryRestResource(exported = false)
public interface BoothRepository extends JpaRepository<Booth, Long> {
}
