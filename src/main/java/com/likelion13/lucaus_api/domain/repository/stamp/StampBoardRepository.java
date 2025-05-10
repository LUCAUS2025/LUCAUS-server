package com.likelion13.lucaus_api.domain.repository.stamp;

import com.likelion13.lucaus_api.domain.entity.stamp.StampBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface StampBoardRepository extends JpaRepository<StampBoard, Long> {

    @Query("SELECT DISTINCT sb FROM StampBoard sb " +
            "JOIN FETCH sb.stampBoardBoothMappings m " +
            "JOIN FETCH m.stampBooth " +
            "WHERE sb.user.id = :userId")
    List<StampBoard> findWithBoothMappingsByUserId(String userId);

    @Query("SELECT DISTINCT sb FROM StampBoard sb " +
            "JOIN FETCH sb.stampBoardBoothMappings m " +
            "JOIN FETCH m.stampBooth " +
            "WHERE sb.user.id = :userId and sb.type = :type ")
    StampBoard findStampBoardByUserIdAndType(String userId, Integer type);



}
