package com.backend.service.models.responses.ai;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoodResponse {
  private boolean success;
  private String message;
  private ImageContext data;
}
