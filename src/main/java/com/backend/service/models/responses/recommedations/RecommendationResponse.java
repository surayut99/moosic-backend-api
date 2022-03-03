package com.backend.service.models.responses.recommedations;

import com.backend.service.models.spotify.responses.SpotifyTrack;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponse {
  private int page;
  private int count;
  private String mood;
  private String keyword;
  private SpotifyTrack[] tracks;
}
