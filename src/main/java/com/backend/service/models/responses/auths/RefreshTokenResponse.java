package com.backend.service.models.responses.auths;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenResponse {
  @SerializedName("access_token")
  private String accessToken;

  @SerializedName("refresh_token")
  private String refreshToken;

  @SerializedName("user_id")
  private String userId;

  @SerializedName("expires_in")
  private String expiresIn;
}
