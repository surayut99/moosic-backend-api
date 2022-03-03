package com.backend.service.models.responses.imageanalyze;


import com.backend.service.models.spotify.responses.SpotifyPlaylist;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlaylistAnalyzedResponse {
  private String mood;
  private String keyword;
  private SpotifyPlaylist playlist;
  private SpotifyTrack[] tracks;

}
