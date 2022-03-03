package com.backend.service.views.posts;

import com.backend.service.views.users.UserPublicView;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public interface PostFeedView {
  UUID getId();

  String getImg();

  UserPublicView getUser();

  long getLikeCount();

  @Value("#{target.getExposedPost().getCreatedAt()}")
  String getCreatedAt();

  @Value("#{target.getExposedPost().getUpdatedAt()}")
  String getUpdatedAt();
}
