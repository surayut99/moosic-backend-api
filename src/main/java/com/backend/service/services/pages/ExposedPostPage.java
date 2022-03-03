package com.backend.service.services.pages;

import com.backend.service.models.entities.ExposedPostModel;
import com.backend.service.models.entities.MusicModel;
import com.backend.service.models.entities.PostModel;
import com.backend.service.repository.InteractionRepository;
import com.backend.service.services.firebase.FirebaseStorageService;
import com.backend.service.services.spotify.SpotifyService;
import com.backend.service.utils.FutureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ExposedPostPage {
  @Autowired
  private InteractionRepository interactionRepo;
  @Autowired
  private FirebaseStorageService firebaseStorageService;
  @Autowired
  private SpotifyService spotifyService;

  private final Logger logger = LoggerFactory.getLogger(ExposedPostModel.class);
  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

  public PageRequest createFeedPageRequest(int page) {
    int PAGE_SIZE = 3;
    return PageRequest.of(
        page,
        PAGE_SIZE,
        Sort.by("post.createdAt").descending()
            .and(Sort.by("post.likeCount")));
  }

  public PageRequest createRankPageRequest(int page) {
    int PAGE_SIZE = 3;
    return PageRequest.of(
        page,
        PAGE_SIZE,
        Sort.by("post.likeCount").descending().and(
            Sort.by("createdAt").ascending()
        ));
  }

  public PageRequest createTopFeedPageRequest(int page, int limit) {
    return PageRequest.of(page, limit);
  }

  public <T> PageImpl<T> castPageAfterQuery(Page<ExposedPostModel> posts, UUID userId, Class<T> type) {
    return new PageImpl<>(
        castListAfterQuery(posts.getContent(), userId, type),
        posts.getPageable(),
        posts.getTotalElements());
  }

  public <T> List<T> castListAfterQuery(List<ExposedPostModel> posts, UUID userId, Class<T> type) {
    List<CompletableFuture<T>> postFutures = posts
        .stream()
        .map(p -> CompletableFuture.supplyAsync(() -> {
          PostModel post = p.getPost();
          MusicModel music = p.getMusic();
          post.setImg(firebaseStorageService.downloadImageAsBase64(post.getImgURL()));
          music.setData(spotifyService.getTrackById(music.getSpotifyId()));

          p.setLiked(interactionRepo.existsInteractionModelByPostIdAndUserId(p.getPost().getId(), userId));

          return type.equals(ExposedPostModel.class) ? type.cast(p) : projectionFactory.createProjection(type, p);
        }))
        .collect(Collectors.toList());

    return FutureUtils.getInstance().solveAllFutures(postFutures);
  }
}
