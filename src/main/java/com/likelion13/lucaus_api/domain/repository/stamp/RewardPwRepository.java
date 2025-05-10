package com.likelion13.lucaus_api.domain.repository.stamp;

import com.likelion13.lucaus_api.domain.entity.stamp.RewardPw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface RewardPwRepository extends JpaRepository<RewardPw, Long> {
    RewardPw findByTypeAndDegree(Integer type, Integer degree);

}
