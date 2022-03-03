package com.backend.service.views.posts;

import com.backend.service.views.playlists.PlaylistEditView;
import com.backend.service.views.users.UserPublicView;

import java.util.List;
import java.util.UUID;

public interface PostEditView extends PostFeedView{

  List<PlaylistEditView> getPlaylists();

}
