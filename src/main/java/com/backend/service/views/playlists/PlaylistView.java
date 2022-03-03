package com.backend.service.views.playlists;

import java.util.UUID;

public interface PlaylistView {
  UUID getId();

  String getName();

  String getMood();

  String getKeyword();
}
