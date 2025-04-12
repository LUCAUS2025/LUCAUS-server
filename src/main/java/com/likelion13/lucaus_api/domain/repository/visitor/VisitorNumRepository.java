package com.likelion13.lucaus_api.domain.repository.visitor;

import com.likelion13.lucaus_api.domain.entity.visitor.VisitorsNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface VisitorNumRepository extends JpaRepository<VisitorsNum, Long> {
    @Query("SELECT v FROM VisitorsNum v ORDER BY v.updatedAt DESC LIMIT 1")
    VisitorsNum findLatestVisitorNum();
}
