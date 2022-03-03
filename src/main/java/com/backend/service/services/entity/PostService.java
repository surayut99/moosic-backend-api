package com.backend.service.services.entity;

import com.backend.service.exceptions.posts.NotFoundPostException;
import com.backend.service.models.entities.PostModel;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.requests.posts.PostCreationRequest;
import com.backend.service.repository.PostRepository;
import com.backend.service.views.posts.PostEditView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PostService {
  @Autowired
  private PostRepository postRepo;

  public PostModel create(PostCreationRequest postCreation, String imagePath, UserModel user) {
    PostModel post = new PostModel();
    post.setUser(user);
    post.setImgURL(imagePath);
    post = postRepo.saveAndFlush(post);

    return post;
  }

  public PostModel update(PostModel data, UUID userId) {
    return data;
  }

  public PostModel getById(UUID postId) throws NotFoundPostException {
    return postRepo.findById(postId).orElseThrow(() -> new NotFoundPostException(postId));
  }

  public PostEditView getEditViewById(UUID postId) throws NotFoundPostException {
    PostModel post = getById(postId);
    return postRepo.getEditViewById(postId).orElseThrow(() -> new NotFoundPostException(postId));
  }

  public boolean isUserOwnedPost(UUID postId, UUID userId) {
    return postRepo.existsPostModelByIdAndUserId(postId, userId);
  }

  public List<PostModel> getAll() {
    return postRepo.findAll();
  }

  public void delete(UUID postId) {
    postRepo.deleteById(postId);
  }

  public void delete(PostModel post) {
    postRepo.delete(post);
  }
}
