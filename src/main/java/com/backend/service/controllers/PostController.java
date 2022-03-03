package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.posts.NotFoundPostException;
import com.backend.service.exceptions.users.UserPermissionException;
import com.backend.service.models.entities.ExposedPostModel;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.requests.posts.PostCreationRequest;
import com.backend.service.models.requests.posts.PostUpdateRequest;
import com.backend.service.models.responses.APIResponse;
import com.backend.service.models.responses.posts.NormalFeedResponse;
import com.backend.service.models.responses.posts.RankedFeedResponse;
import com.backend.service.services.firebase.FirebaseStorageService;
import com.backend.service.services.implementations.PostManagementService;
import com.backend.service.utils.TimeUtils;
import com.backend.service.views.exposedPosts.ExposedPostFeedView;
import com.backend.service.views.exposedPosts.ExposedTop5View;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/posts")
public class PostController {
  @Autowired
  private FirebaseStorageService firebaseStorageService;
  @Autowired
  private PostManagementService postManagementService;

  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

  @PostMapping("/create")
  public ResponseEntity<APIResponse> createPost(
      HttpServletRequest request,
      @RequestPart("image") MultipartFile file,
      @RequestPart("data") String postCreationData) throws IOException, NotFoundPostException {
    // parse string to object
    ObjectMapper objectMapper = new ObjectMapper();
    PostCreationRequest postCreation = objectMapper.readValue(postCreationData, PostCreationRequest.class);

    // get credential from request attribute
    UserModel user = (UserModel) request.getAttribute("user");

    // upload image to firebase cloud storage, then return data as blob path
    String imagePath = firebaseStorageService.uploadImage(file.getBytes(), user.getId());
    ExposedPostFeedView post;

    try {
      // write data with given necessary data
      ExposedPostModel exposedPost = postManagementService.createPost(postCreation, imagePath, user);
      exposedPost.getPost().setImg(firebaseStorageService.downloadImageAsBase64(imagePath));
      post = projectionFactory.createProjection(ExposedPostFeedView.class, exposedPost);
    } catch (RuntimeException e) {
      // for transaction goes rollback, remove file on firebase storage with given image path
      firebaseStorageService.deleteImage(imagePath);
      // then throw exception out
      throw e;
    }

    APIResponse response = APIResponse.builder()
        .success(true)
        .message("Post with playlists and tracks are created successfully")
        .data(post)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @PutMapping("/update/{id}")
  public ResponseEntity<APIResponse> updatePost(
      @PathVariable UUID id,
      @Valid @RequestBody PostUpdateRequest data,
      HttpServletRequest request) throws MoosicException, IOException {

    UserModel user = (UserModel) request.getAttribute("user");

    ExposedPostFeedView post = projectionFactory.createProjection(
        ExposedPostFeedView.class,
        postManagementService.updatePost(data, id, user));

    APIResponse response = APIResponse.builder()
        .success(true)
        .message(String.format("Post id: %s is saved successfully", id))
        .data(post)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/feed")
  public ResponseEntity<APIResponse> getPostFeed(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      HttpServletRequest request) {

    UserModel user = (UserModel) request.getAttribute("user");
    Object data;

    if (!TimeUtils.getInstance().isWeekEndDay()) {
      Page<ExposedPostFeedView> result = postManagementService.getPostFeed(page, user);
      data = NormalFeedResponse.builder()
          .page(page)
          .totalPage(result.getTotalPages())
          .count(result.getNumberOfElements())
          .posts(result.getContent())
          .topPosts(postManagementService.getTopMood(user, ExposedTop5View.class))
          .build();
    } else {
      data = RankedFeedResponse.builder()
          .posts(postManagementService.getTopPost(user, ExposedPostFeedView.class))
          .topPosts(postManagementService.getTopMood(user, ExposedTop5View.class))
          .build();
    }

    APIResponse response = APIResponse.builder()
        .success(true)
        .message("Get feed post successfully")
        .data(data)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<APIResponse> getPostById(
      @PathVariable UUID id,
      @RequestParam(defaultValue = "feed") String type,
      HttpServletRequest request) throws MoosicException, ExecutionException, InterruptedException {
    UserModel user = (UserModel) request.getAttribute("user");
    Object data;

    if ("feed".equals(type)) {
      data = postManagementService.getPostById(id, user, true, ExposedPostFeedView.class);
    } else {
      data = postManagementService.getPostEditViewById(id);
    }

    APIResponse response = APIResponse.builder()
        .success(true)
        .message(String.format("Get post id: %s for %s successfully", id, type))
        .data(data)
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<APIResponse> deletePost(@PathVariable UUID id, HttpServletRequest request) throws UserPermissionException, NotFoundPostException {
    UserModel user = (UserModel) request.getAttribute("user");

    // delete post and return image url on firebase store
    String imageURL = postManagementService.deletePost(id, user);
    boolean result = firebaseStorageService.deleteImage(imageURL);

    APIResponse response = APIResponse.builder()
        .success(true)
        .message(String.format("Post id: %s deleted successfully", id))
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
