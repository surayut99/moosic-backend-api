package com.backend.service.services.entity;

import com.backend.service.exceptions.posts.NotFoundPostException;
import com.backend.service.models.entities.ExposedPostModel;
import com.backend.service.models.entities.MusicModel;
import com.backend.service.models.entities.PlaylistModel;
import com.backend.service.models.entities.PostModel;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.backend.service.repository.ExposedPostRepository;
import com.backend.service.repository.InteractionRepository;
import com.backend.service.services.firebase.FirebaseStorageService;
import com.backend.service.services.pages.ExposedPostPage;
import com.backend.service.services.specification.ExposedPostSpecification;
import com.backend.service.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class ExposedPostService {
  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
  @Autowired
  private InteractionRepository interactionRepo;
  @Autowired
  private ExposedPostRepository exposedRepo;
  @Autowired
  private ExposedPostPage exposedPostPage;
  @Autowired
  private ExposedPostSpecification exposedPostSpecification;
  @Autowired
  private FirebaseStorageService firebaseStorageService;

  // General
  public ExposedPostModel create(PostModel post, PlaylistModel playlist, MusicModel music) {
    ExposedPostModel exposedPost = new ExposedPostModel();

    exposedPost.setPost(post);
    exposedPost.setPlaylist(playlist);
    exposedPost.setMusic(music);

    return exposedRepo.saveAndFlush(exposedPost);
  }

  public ExposedPostModel update(ExposedPostModel post) {
    return exposedRepo.saveAndFlush(post);
  }

  public void deletePost(ExposedPostModel exposedPost) {
    exposedRepo.delete(exposedPost);
  }

  // Get all
  public List<ExposedPostModel> getAll() {
    return exposedRepo.findAll();
  }

  public <T> List<T> getAll(UUID userId, Class<T> type) {
    List<ExposedPostModel> posts = getAll();

    return exposedPostPage.castListAfterQuery(posts, userId, type);
  }


  // Get Top post
  public Page<ExposedPostModel> getTopMood() {
    return exposedRepo.findTopMood(
        TimeUtils.getInstance().getMondayTimestampOfWeek(),
        exposedPostPage.createTopFeedPageRequest(0, 5));
  }

  public <T> Page<T> getTopMood(UUID userId, Class<T> type) {
    Page<ExposedPostModel> posts = getTopMood();

    return exposedPostPage.castPageAfterQuery(posts, userId, type);
  }

  public Page<ExposedPostModel> getTopPost() {
    return exposedRepo.findAll(
        exposedPostSpecification.findByCreatedAtGE(TimeUtils.getInstance().getMondayTimestampOfWeek()),
        exposedPostPage.createRankPageRequest(0));
  }

  public <T> Page<T> getTopPost(UUID userId, Class<T> type) {
    Page<ExposedPostModel> posts = getTopPost();

    return exposedPostPage.castPageAfterQuery(posts, userId, type);
  }


  // Get Feed
  public <T> Page<T> getAllFeed(int page, UUID userId, Class<T> type) {
    Page<ExposedPostModel> posts = exposedRepo.findAll(
        exposedPostSpecification.findByCreatedAtGE(TimeUtils.getInstance().getMondayTimestampOfWeek()),
        exposedPostPage.createFeedPageRequest(page));

    return exposedPostPage.castPageAfterQuery(posts, userId, type);
  }


  // Search by mood and title keyword
  public Page<ExposedPostModel> getAllByKeywords(int page, List<String> keywords, boolean isTitleType) {
    PageRequest pageRequest = exposedPostPage.createFeedPageRequest(page);

    if (isTitleType) {
      return exposedRepo.findAll(pageRequest);
    }
    Specification<ExposedPostModel> specification = exposedPostSpecification.findByMood(keywords);

    return exposedRepo.findAll(specification, pageRequest);
//    return exposedRepo.findAll(
//        exposedPostSpecification.finder(keywords, isTitleType),
//        exposedPostPage.createFeedPageRequest(page)
//    );
  }

  public <T> Page<T> getAllByKeywords(int page, UUID userId, List<String> keywords, boolean isTitleType, Class<T> type) {
    Page<ExposedPostModel> posts = getAllByKeywords(page, keywords, isTitleType);
    return exposedPostPage.castPageAfterQuery(posts, userId, type);
  }


  // Get by ID
  public ExposedPostModel getByPostId(UUID postId, UUID userId) throws NotFoundPostException {
    ExposedPostModel post = exposedRepo.findByPostId(postId).orElseThrow(() -> new NotFoundPostException(postId));
    post.setLiked(interactionRepo.existsInteractionModelByPostIdAndUserId(postId, userId));

    return post;
  }

  public <T> T getByPostId(UUID postId, UUID userId, boolean loadImage, Class<T> type) throws NotFoundPostException {
    ExposedPostModel result = getByPostId(postId, userId);

    if (loadImage) {
      PostModel post = result.getPost();
      post.setImg(firebaseStorageService.downloadImageAsBase64(post.getImgURL()));
    }

    return type.equals(ExposedPostModel.class) ? type.cast(result) : projectionFactory.createProjection(type, result);
  }

  public <T> T getByPostId(UUID postId, UUID userId, Class<T> type) throws NotFoundPostException {
    return getByPostId(postId, userId, false, type);
  }
}
