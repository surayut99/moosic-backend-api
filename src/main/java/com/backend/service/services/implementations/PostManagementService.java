package com.backend.service.services.implementations;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.musics.NotFoundMusicException;
import com.backend.service.exceptions.playlists.NotFoundPlaylistException;
import com.backend.service.exceptions.posts.NotFoundPostException;
import com.backend.service.exceptions.users.UserPermissionException;
import com.backend.service.models.entities.*;
import com.backend.service.models.requests.posts.PostCreationRequest;
import com.backend.service.models.requests.posts.PostUpdateRequest;
import com.backend.service.models.responses.imageanalyze.PlaylistAnalyzedResponse;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.backend.service.services.entity.*;
import com.backend.service.services.firebase.FirebaseStorageService;
import com.backend.service.services.spotify.SpotifyService;
import com.backend.service.utils.FutureUtils;
import com.backend.service.views.exposedPosts.ExposedPostFeedView;
import com.backend.service.views.posts.PostEditView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PostManagementService {
  @Autowired
  private PostService postService;
  @Autowired
  private PlaylistService playlistService;
  @Autowired
  private MusicService musicService;
  @Autowired
  private ExposedPostService exposedPostService;
  @Autowired
  private InteractionService interactionService;

  @Autowired
  private FirebaseStorageService firebaseStorageService;
  @Autowired
  private SpotifyService spotifyService;

  private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

  @Transactional
  public ExposedPostModel createPost(PostCreationRequest postCreation, String imagePath, UserModel user) throws NotFoundPostException {
    // go to create post with necessary data
    PostModel post = postService.create(postCreation, imagePath, user);

//    // get future instance
//    FutureUtils futureUtils = FutureUtils.getInstance();
//
//    // loop to create playlist as async event
//    List<CompletableFuture<PlaylistModel>> playlistFutures = Arrays.stream(postCreation.getPlaylists())
//        .map(pl -> CompletableFuture.supplyAsync(() -> {
//
//          // create new playlist for post
//          PlaylistModel playlist = playlistService.create(pl, post);
//
//          // loop to create music for playlist as async event
//          List<CompletableFuture<MusicModel>> musicFutures = Arrays
//              .stream(pl.getTracks())
//              .map(track -> CompletableFuture.supplyAsync(() -> musicService.create(track, playlist)))
//              .collect(Collectors.toList());
//
//          // solve and get data from music futures, then set to playlist
//          playlist.setMusics(futureUtils.solveAllFutures(musicFutures));
//
//          // return play with satisfied list of music
//          return playlist;
//        }))
//        .collect(Collectors.toList());
//
//    // solve all playlist and set it to post
//    post.setPlaylists(futureUtils.solveAllFutures(playlistFutures));


    // set created playlist to post
    post.setPlaylists(Arrays
        .stream(postCreation.getPlaylists())
        .map(pl -> {
          // create playlist related to post
          PlaylistModel playlist = playlistService.create(pl, post);

          // create music related to playlists
          playlist.setMusics(Arrays
              .stream(pl.getTracks())
              .map(track -> musicService.create(track, playlist))
              .collect(Collectors.toList()));

          return playlist;
        })
        .collect(Collectors.toList()));

    // get saved playlist and model to create new exposed post
    int selectedPlayListIdx = postCreation.getPlaylistIndex();
    int selectedMusicIdx = postCreation.getMusicIndex();
    PlaylistAnalyzedResponse selectedPlaylist = postCreation.getPlaylists()[selectedPlayListIdx];
    SpotifyTrack selectedTrack = selectedPlaylist.getTracks()[selectedMusicIdx];
    PlaylistModel savedPlaylist = post.getPlaylists().get(postCreation.getPlaylistIndex());
    MusicModel savedMusic = savedPlaylist.getMusics().get(postCreation.getMusicIndex());

    // set spotify track data to model before saving exposed post
    savedMusic.setData(selectedTrack);

    // create relationship between post, playlist and music in database
    return exposedPostService.create(post, savedPlaylist, savedMusic);
  }

  @Transactional
  public void likePost(UUID postId, UserModel user) throws NotFoundPostException {
    PostModel post = postService.getById(postId);
    interactionService.create(post, user);

    post.setLikeCount(interactionService.getCountByPost(post));
  }

  @Transactional
  public void unLikePost(UUID postId, UserModel user) throws MoosicException {
    PostModel post = postService.getById(postId);
    interactionService.delete(post, user);

    post.setLikeCount(interactionService.getCountByPost(post));
  }

  @Transactional
  public String deletePost(UUID postId, UserModel user) throws NotFoundPostException, UserPermissionException {
    ExposedPostModel exposedPost = exposedPostService.getByPostId(postId, user.getId(), ExposedPostModel.class);
    PostModel post = exposedPost.getPost();
    String imgURL = post.getImgURL();

    if (!post.getUser().getId().equals(user.getId())) {
      throw new UserPermissionException();
    }

    interactionService.delete(post);
    exposedPostService.deletePost(exposedPost);

    return imgURL;
  }

  @Transactional
  public ExposedPostModel updatePost(PostUpdateRequest data, UUID postId, UserModel user) throws MoosicException, IOException {
    // check if this user own this post
    if (!postService.isUserOwnedPost(postId, user.getId())) {
      throw new UserPermissionException();
    }

    // check if selected playlist has relation to this post
    if (!playlistService.isPlaylistedRelatedToPost(UUID.fromString(data.getPlaylistId()), postId)) {
      throw new NotFoundPlaylistException(data.getPlaylistId());
    }

    // check if selected music has relation to selected playlist
    if (!musicService.isMusicRelatedToPlaylist(UUID.fromString(data.getMusicId()), UUID.fromString(data.getPlaylistId()))) {
      throw new NotFoundMusicException(data.getMusicId());
    }

    ExposedPostModel post = exposedPostService.getByPostId(postId, user.getId());
    post.setPlaylist(playlistService.getById(UUID.fromString(data.getPlaylistId())));
    post.setMusic(musicService.getById(UUID.fromString(data.getMusicId())));
    post.getPost().setImg(firebaseStorageService.downloadImageAsBase64(post.getPost().getImgURL()));
    post.getMusic().setData(spotifyService.getTrackById(post.getMusic().getSpotifyId()));

    return exposedPostService.update(post);
  }


  // Get post for editing
  public PostEditView getPostEditViewById(UUID postId) throws MoosicException, ExecutionException, InterruptedException {
    PostModel post = postService.getById(postId);

    // make request to get image as async request
    CompletableFuture<String> imageFuture = CompletableFuture.supplyAsync(() -> firebaseStorageService.downloadImageAsBase64(post.getImgURL()));

    // get future util instance
    FutureUtils futureUtils = FutureUtils.getInstance();

    // loop to create future for each playlist
    List<CompletableFuture<PlaylistModel>> playlistFutures = post.getPlaylists()
        .stream()
        .map(p -> CompletableFuture.supplyAsync(() -> {
          // loop to create future for each music
          List<CompletableFuture<MusicModel>> musicFutures = p.getMusics()
              .stream()
              .map(m -> CompletableFuture.supplyAsync(() -> {
                // go to fetch Spotify track data and set to music object
                m.setData(spotifyService.getTrackById(m.getSpotifyId()));
                // then return music object
                return m;
              }))
              .collect(Collectors.toList());

          // after collecting future music, go to solve and get result from futures
          List<MusicModel> solvedMusics = futureUtils.solveAllFutures(musicFutures);
          // set solved musics from futures to playlist
          p.setMusics(solvedMusics);

          // return playlist containing solved music futures
          return p;
        }))
        .collect(Collectors.toList());

    // after collection playlist futures, go to solve and get result from futures
    List<PlaylistModel> solvedPlaylists = futureUtils.solveAllFutures(playlistFutures);

    // set solved playlist to post
    post.setPlaylists(solvedPlaylists);

    // set image based 64 string from future
    post.setImg(imageFuture.get());

    // cast object to projection type
    return projectionFactory.createProjection(PostEditView.class, post);
  }


  // get all post
  public Page<ExposedPostFeedView> getPostFeed(int page, UserModel user) {
    return exposedPostService.getAllFeed(page, user.getId(), ExposedPostFeedView.class);
  }


  // Get by ID
  public ExposedPostModel getPostById(UUID postId, UserModel user) throws NotFoundPostException {
    return exposedPostService.getByPostId(postId, user.getId());
  }

  public <T> T getPostById(UUID postId, UserModel user, boolean loadImage, Class<T> type) throws NotFoundPostException {
    ExposedPostModel post = exposedPostService.getByPostId(postId, user.getId(), loadImage, ExposedPostModel.class);
    MusicModel music = post.getMusic();
    music.setData(spotifyService.getTrackById(music.getSpotifyId()));

    return projectionFactory.createProjection(type, post);
  }


  // Get by keyword including moods and title
  public Page<ExposedPostFeedView> getByKeywords(int page, UserModel user, String keyword, boolean isTitleType) {
    final int KEYWORD_LIMIT = 9;

    List<String> keywords = Arrays.asList(keyword.split(" "));
    keywords = keywords.stream().limit(KEYWORD_LIMIT).collect(Collectors.toList());
    keywords.add(keyword);

    List<String> finalKeywords = new ArrayList<>(keywords);
    Page<ExposedPostFeedView> postPages = exposedPostService.getAllByKeywords(page, user.getId(), keywords, isTitleType, ExposedPostFeedView.class);

    if (!isTitleType) {
      return postPages;
    }

    List<ExposedPostFeedView> completedPosts = postPages.getContent()
        .stream()
        .filter(post -> {
          for (String key : finalKeywords) {
            if (post.getMusic().getTitle().toLowerCase().contains(key.toLowerCase())) return true;
          }

          return false;
        })
        .map((post) -> projectionFactory.createProjection(ExposedPostFeedView.class, post))
        .collect(Collectors.toList());

    return new PageImpl<>(completedPosts, postPages.getPageable(), postPages.getTotalElements());
  }


  // Get top weekly posts
  public <T> List<T> getTopMood(UserModel user, Class<T> type) {
    return exposedPostService.getTopMood(user.getId(), type).getContent();
  }

  public <T> List<T> getTopPost(UserModel user, Class<T> type) {
    return exposedPostService.getTopPost(user.getId(), type).getContent();
  }
}
