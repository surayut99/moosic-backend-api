package com.backend.service.exceptions.auth;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundAccessTokenException extends MoosicException {
  public NotFoundAccessTokenException() {
    super("Not found access token for authorization", HttpStatus.UNAUTHORIZED);
  }
}