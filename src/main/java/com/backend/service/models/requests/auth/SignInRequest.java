package com.backend.service.models.requests.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class SignInRequest {
  private String accessToken;
  private String refreshToken;
  private long expirationTime;
  private String uid;
  private String displayName;
  private String email;
  private String photoURL;
}
