package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.posts.LikeLimitationException;
import com.backend.service.exceptions.posts.NotFoundPostException;
import com.backend.service.models.entities.ExposedPostModel;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.responses.APIResponse;
import com.backend.service.services.implementations.PostManagementService;
import com.backend.service.views.exposedPosts.ExposedPostFeedView;
import com.backend.service.views.interactions.InteractionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/interactions")
public class InteractionController {
  @Autowired
  PostManagementService postManagementService;

  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

  @PostMapping("/create")
  public ResponseEntity<APIResponse> likePost(@RequestParam(name = "post-id") UUID postId, HttpServletRequest request) throws NotFoundPostException, LikeLimitationException {
    // get user credential from request attributes
    UserModel user = (UserModel) request.getAttribute("user");

    // create entry to interaction table
    try {
      postManagementService.likePost(postId, user);
    } catch (RuntimeException e) {
      throw new LikeLimitationException();
    }

    // get post information after transaction committed
    ExposedPostModel post = postManagementService.getPostById(postId, user);
    InteractionView castedPost = projectionFactory.createProjection(InteractionView.class, post);

    APIResponse response = APIResponse.builder()
        .success(true)
        .message("Post like interaction created successfully")
        .data(castedPost)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<APIResponse> unLikePost(@RequestParam(name = "post-id") UUID postId, HttpServletRequest request) throws MoosicException {
    // get user credential from request attributes
    UserModel user = (UserModel) request.getAttribute("user");

    postManagementService.unLikePost(postId, user);

    // get post information after transaction committed
    ExposedPostModel post = postManagementService.getPostById(postId, user);
    InteractionView castedPost = projectionFactory.createProjection(InteractionView.class, post);

    APIResponse response = APIResponse.builder()
        .success(true)
        .message("Post like interaction deleted successfully")
        .data(castedPost)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
