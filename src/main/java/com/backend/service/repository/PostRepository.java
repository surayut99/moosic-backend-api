package com.backend.service.repository;

import com.backend.service.models.entities.PostModel;
import com.backend.service.views.posts.PostEditView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostModel, UUID> {
  @Query("select p from PostModel p where p.id=?1")
  Optional<PostEditView> getEditViewById(UUID postId);

  boolean existsPostModelByIdAndUserId(UUID postId, UUID userId);
}
