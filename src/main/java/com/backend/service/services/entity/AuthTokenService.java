package com.backend.service.services.entity;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.auth.InvalidRefreshTokenException;
import com.backend.service.exceptions.users.NotFoundUserException;
import com.backend.service.models.authentication.ClaimCredential;
import com.backend.service.models.entities.AuthTokenModel;
import com.backend.service.models.requests.auth.TokenRegisterRequest;
import com.backend.service.models.responses.auths.RefreshTokenResponse;
import com.backend.service.repository.AuthTokenRepository;
import com.backend.service.utils.TokenUtils;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AuthTokenService {
  @Autowired
  private AuthTokenRepository authRepo;

  public AuthTokenModel createToken(TokenRegisterRequest credential, ClaimCredential claim) {
    // set new token with given user credential from client side
    AuthTokenModel token = authRepo.findByUserId(claim.getId()).orElseGet(AuthTokenModel::new);

    token.setAccessToken(credential.getAccessToken());
    token.setRefreshToken(credential.getRefreshToken());
    token.setUserId(claim.getId());
    token.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusHours(1)).toString());

    return authRepo.saveAndFlush(token);
  }

  public AuthTokenModel refreshToken(UUID userId, String refreshToken) throws MoosicException, FirebaseAuthException {
    // get token dat by user id
    AuthTokenModel token = authRepo.findByUserId(userId).orElseThrow(NotFoundUserException::new);

    if (!token.getRefreshToken().equals(refreshToken)) {
      throw new InvalidRefreshTokenException();
    }

    // use refresh token to get new access token from Google
    RefreshTokenResponse response = TokenUtils.getInstance().refreshToken(token.getRefreshToken());

    // update new tokens and expired in
    token.setAccessToken(response.getAccessToken());
    token.setRefreshToken(response.getRefreshToken());
    long expiresIn = Long.parseLong(response.getExpiresIn());
    long currentSec = Instant.now().getEpochSecond();
    // String expiredAt = Instant.ofEpochSecond(currentSec + expiresIn).toString();
    token.setExpiredAt(Timestamp.valueOf(LocalDateTime.now().plusHours(1)).toString());
    authRepo.saveAndFlush(token);

    return token;
  }

  public void revokeToken(UUID userId) throws MoosicException {
    // revoke token by uid
    try {
      TokenUtils.getInstance().revokeToken(userId.toString());
    } catch (FirebaseAuthException ignored) {
    } finally {
      // then delete token entry in database by uuid
      authRepo.deleteByUserId(userId);
    }
  }

  public AuthTokenModel update(AuthTokenModel data, UUID userId) {
    return data;
  }

  public AuthTokenModel getById(UUID id) {
    return authRepo.getById(id);
  }

  public List<AuthTokenModel> getAll() {
    return authRepo.findAll();
  }

}
