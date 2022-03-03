package com.backend.service.models.spotify.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpotifyRecommendationQuery {
  private String seed_tracks;
  private String seed_artists;
  private String seed_genres;
  private int limit;
  private int offset;
}
