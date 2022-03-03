package com.backend.service.repository;

import com.backend.service.models.entities.ExposedPostModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ExposedPostRepository extends JpaRepository<ExposedPostModel, UUID>, JpaSpecificationExecutor<ExposedPostModel> {
  Optional<ExposedPostModel> findByPostId(UUID postId);

  @Query(value = "select * from top_5_mood tm where created_at >= :fromDate", nativeQuery = true)
  Page<ExposedPostModel> findTopMood(@Param("fromDate") String fromDate, Pageable page);
}
