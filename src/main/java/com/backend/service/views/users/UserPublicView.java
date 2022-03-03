package com.backend.service.views.users;

import java.util.UUID;

public interface UserPublicView {
  UUID getId();

  String getDisplayName();

  String getEmail();

  String getUsername();

  String getPhotoUrl();
}
