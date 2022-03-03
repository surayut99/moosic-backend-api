package com.backend.service.models.spotify.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpotifySeed {
  //    @Expose
  private int initialPoolSize;

  //    @Expose
  private int afterFilteringSize;

  //    @Expose
  private int afterRelinkingSize;

  //    @Expose
  private String id;

  //    @Expose
  private String type;

  //    @Expose
  private String href;
}
