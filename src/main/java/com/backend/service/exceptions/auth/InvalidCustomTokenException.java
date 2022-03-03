package com.backend.service.exceptions.auth;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class InvalidCustomTokenException extends MoosicException {
  public InvalidCustomTokenException() {
    super("Invalid custom token not allowed to authorization", HttpStatus.UNAUTHORIZED);
  }
}
