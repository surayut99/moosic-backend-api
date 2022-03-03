package com.backend.service.services.entity;

import com.backend.service.exceptions.interactions.NotFoundInteractionException;
import com.backend.service.models.entities.InteractionModel;
import com.backend.service.models.entities.PostModel;
import com.backend.service.models.entities.UserModel;
import com.backend.service.repository.InteractionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class InteractionService {
  @Autowired
  private InteractionRepository interactionRepo;

  public InteractionModel create(PostModel post, UserModel user) {
    InteractionModel interaction = new InteractionModel();
    interaction.setPost(post);
    interaction.setUser(user);

    return interactionRepo.saveAndFlush(interaction);
  }

  public void update(UUID postId, UUID userId) {
  }

  public List<InteractionModel> getByPostId(PostModel post) {
    return interactionRepo.findByPost(post);
  }

  public List<InteractionModel> getByUserId(UserModel user) {
    return interactionRepo.findByUser(user);
  }

  public void delete(InteractionModel interaction) {
    interactionRepo.delete(interaction);
  }

  public void delete(PostModel post) {
    interactionRepo.deleteByPost(post);
  }

  public void delete(PostModel post, UserModel user) throws NotFoundInteractionException {
    if (isUserLiked(post, user)) {
      interactionRepo.deleteByPostAndUser(post, user);
      return;
    }

    throw new NotFoundInteractionException();
  }

  public long getCountByPost(PostModel postModel) {
    return interactionRepo.countByPost(postModel);
  }

  public boolean isUserLiked(PostModel post, UserModel user) {
    return interactionRepo.existsInteractionModelByPostAndUser(post, user);
  }

  public boolean isUserLiked(UUID postId, UUID userId) {
    return interactionRepo.existsInteractionModelByPostIdAndUserId(postId, userId);
  }
}
