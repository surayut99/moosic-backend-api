package com.backend.service.exceptions.auth;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundCustomTokenException extends MoosicException {
  public NotFoundCustomTokenException() {
    super("Not found custom token for authorization", HttpStatus.UNAUTHORIZED);
  }
}
