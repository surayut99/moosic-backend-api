package com.backend.service.repository;

import com.backend.service.models.entities.InteractionModel;
import com.backend.service.models.entities.PostModel;
import com.backend.service.models.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InteractionRepository extends JpaRepository<InteractionModel, UUID> {
  List<InteractionModel> findByPostAndUser(PostModel post, UserModel user);

  List<InteractionModel> findByPost(PostModel post);

  List<InteractionModel> findByUser(UserModel post);

  void deleteByPostAndUser(PostModel post, UserModel user);

  void deleteByPost(PostModel post);

  long countByPost(PostModel post);

  long countByPostAndUser(PostModel post, UserModel user);

  boolean existsInteractionModelByPostAndUser(PostModel post, UserModel user);

  boolean existsInteractionModelByPostIdAndUserId(UUID post, UUID user);
}
