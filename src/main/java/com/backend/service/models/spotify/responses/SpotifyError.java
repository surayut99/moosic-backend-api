package com.backend.service.models.spotify.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SpotifyError {
  private int status;
  private String message;
}
