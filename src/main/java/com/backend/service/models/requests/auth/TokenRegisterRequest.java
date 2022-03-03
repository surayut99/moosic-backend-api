package com.backend.service.models.requests.auth;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokenRegisterRequest {
  private String accessToken;
  private String refreshToken;
  private String exUid;
  private long expiredAt;
}
