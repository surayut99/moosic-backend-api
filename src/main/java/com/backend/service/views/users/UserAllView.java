package com.backend.service.views.users;

public interface UserAllView extends UserExtendedView {
  String getUid();

  String getCreatedAt();

  String getUpdatedAt();
}