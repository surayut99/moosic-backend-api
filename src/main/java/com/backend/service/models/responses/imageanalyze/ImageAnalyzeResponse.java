package com.backend.service.models.responses.imageanalyze;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageAnalyzeResponse {
  List<PlaylistAnalyzedResponse> playlists;
  List<String> keywords;
}
