package com.backend.service.exceptions.users;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class InactiveUserException extends MoosicException {
  public InactiveUserException() {
    super("Inactive user cannot be allowed", HttpStatus.BAD_REQUEST);
  }
}
