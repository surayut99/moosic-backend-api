package com.backend.service.models.spotify.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpotifyPlaylist {
  @SerializedName("href")
  private String url;
  private String name;
  private String tracksURL;
  //    private int total;
  private String externalURL;
}
