package com.backend.service.models.spotify.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpotifyTrack {
  @Expose
  private String id;

  @Expose
  @SerializedName(value = "name")
  private String title;

  private String[] artists;
  private String album;
  private String albumCoverURL;

  @Expose
  @SerializedName(value = "preview_url")
  private String previewURL;
  private String trackURL;
  private String[] artistURLs;
  private String albumURL;
}
