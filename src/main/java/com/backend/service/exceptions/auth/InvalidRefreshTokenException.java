package com.backend.service.exceptions.auth;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends MoosicException {
  public InvalidRefreshTokenException() {
    super("Refresh token is invalid", HttpStatus.UNAUTHORIZED);
  }
}
