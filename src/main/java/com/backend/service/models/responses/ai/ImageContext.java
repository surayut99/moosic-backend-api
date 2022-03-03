package com.backend.service.models.responses.ai;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ImageContext {
  private List<String> moods;
  private String time;
  private String weather;
}
