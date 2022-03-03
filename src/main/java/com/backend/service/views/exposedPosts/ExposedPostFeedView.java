package com.backend.service.views.exposedPosts;

import com.backend.service.views.musics.MusicView;
import com.backend.service.views.playlists.PlaylistView;
import com.backend.service.views.posts.PostFeedView;

public interface ExposedPostFeedView {
  PostFeedView getPost();

  MusicView getMusic();

  PlaylistView getPlaylist();

  boolean getLiked();
}
