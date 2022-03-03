package com.backend.service.utils;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.responses.auths.RefreshTokenResponse;
import com.backend.service.services.firebase.GoogleAPITokenService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.gson.Gson;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {
  private static TokenUtils tokenUtils;
  private final GoogleAPITokenService googleApiTokenService;

  private TokenUtils() {
    googleApiTokenService = SpringUtils.ctx.getBean(GoogleAPITokenService.class);
  }

  public static TokenUtils getInstance() {
    if (tokenUtils == null) {
      tokenUtils = new TokenUtils();
    }

    return tokenUtils;
  }

  public <T> T decodeToken(String token, Class<T> type) {
    String[] chunks = token.split("\\.");

    Base64.Decoder decoder = Base64.getUrlDecoder();
    String payload = new String(decoder.decode(chunks[1]));
    Gson gson = new Gson();

    return gson.fromJson(payload, type);
  }

  public void revokeToken(String userId) throws FirebaseAuthException {
    FirebaseAuth.getInstance().revokeRefreshTokens(userId);
  }

  public RefreshTokenResponse refreshToken(String refreshToken) throws MoosicException {
    return googleApiTokenService.refreshToken(refreshToken);
  }

  public FirebaseToken verifyToken(String token) throws FirebaseAuthException {
    return FirebaseAuth.getInstance().verifyIdToken(token);
  }

  public String createCustomToken(String firebaseUid, UserModel user) throws FirebaseAuthException {
    Map<String, Object> claims = new HashMap<>();
    claims.put("moosic_id", user.getId().toString());
    claims.put("is_active", user.isActive());

    return FirebaseAuth.getInstance().createCustomToken(firebaseUid, claims);
  }
}
