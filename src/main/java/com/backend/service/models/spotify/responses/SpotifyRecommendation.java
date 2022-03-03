package com.backend.service.models.spotify.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyRecommendation {
  private SpotifySeed[] seeds;
  private SpotifyTrack[] tracks;
}
