package com.backend.service.models.responses.posts;

import com.backend.service.models.entities.MusicModel;
import com.backend.service.models.entities.PlaylistModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaylistSavedResponse {
  private PlaylistModel playlist;
  private List<MusicModel> musics;
}
