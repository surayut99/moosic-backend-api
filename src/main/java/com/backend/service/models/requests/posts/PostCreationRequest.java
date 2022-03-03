package com.backend.service.models.requests.posts;


import com.backend.service.models.responses.imageanalyze.PlaylistAnalyzedResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostCreationRequest {
  private int musicIndex;
  private int playlistIndex;
  //    private String mood;
//    private SpotifyTrack music;
  private PlaylistAnalyzedResponse[] playlists;
}
