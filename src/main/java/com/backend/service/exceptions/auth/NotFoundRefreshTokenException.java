package com.backend.service.exceptions.auth;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundRefreshTokenException extends MoosicException {

  public NotFoundRefreshTokenException() {
    super("Not found token refresh for obtaining new access token", HttpStatus.UNAUTHORIZED);
  }
}
