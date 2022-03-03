package com.backend.service.models.responses.posts;

import com.backend.service.views.exposedPosts.ExposedPostFeedView;
import com.backend.service.views.exposedPosts.ExposedTop5View;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NormalFeedResponse {
  private int page;
  private int totalPage;
  private int count;
  private List<ExposedPostFeedView> posts;
  private List<ExposedTop5View> topPosts;
}
