package com.backend.service.views.playlists;

import com.backend.service.views.musics.MusicView;

import java.util.List;
import java.util.UUID;

public interface PlaylistEditView extends PlaylistView {
  List<MusicView> getMusics();
}
