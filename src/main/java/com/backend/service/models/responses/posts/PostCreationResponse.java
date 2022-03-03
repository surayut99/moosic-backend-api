package com.backend.service.models.responses.posts;

import com.backend.service.models.entities.PostModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostCreationResponse {
  private PostModel post;
  private List<PlaylistSavedResponse> playlistSavedResponse;
}
