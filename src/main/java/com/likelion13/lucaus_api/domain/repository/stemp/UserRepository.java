package com.likelion13.lucaus_api.domain.repository.stemp;

import com.likelion13.lucaus_api.domain.entity.stemp.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsById(String id);
    Optional<User> findById(String id);
}
