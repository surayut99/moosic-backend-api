package com.backend.service.views.users;

public interface UserExtendedView extends UserPublicView {
  String getBio();

  String[] getGenres();
}
