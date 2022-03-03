package com.backend.service.models.requests.posts;

import com.backend.service.validations.TypeValidation;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PostUpdateRequest {
  @NotNull(message = "music id is required")
  @Pattern(regexp = TypeValidation.UUIDV4_PATTERN, message = "music id is invalid")
  private String musicId;

  @NotNull(message = "playlist id is required")
  @Pattern(regexp = TypeValidation.UUIDV4_PATTERN, message = "playlist id is invalid")
  private String playlistId;
}
