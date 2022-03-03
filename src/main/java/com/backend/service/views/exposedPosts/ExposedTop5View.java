package com.backend.service.views.exposedPosts;

import com.backend.service.views.musics.MusicView;
import com.backend.service.views.playlists.PlaylistTop5View;
import com.backend.service.views.posts.PostTop5View;

public interface ExposedTop5View {
  PostTop5View getPost();

  MusicView getMusic();

  PlaylistTop5View getPlaylist();
}
