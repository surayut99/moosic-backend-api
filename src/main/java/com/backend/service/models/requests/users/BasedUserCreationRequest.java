package com.backend.service.models.requests.users;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BasedUserCreationRequest {
  private String bio = "";
  private String[] genres = {};
  private String username;
}
