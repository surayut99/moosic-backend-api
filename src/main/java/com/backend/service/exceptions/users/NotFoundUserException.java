package com.backend.service.exceptions.users;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundUserException extends MoosicException {
  public NotFoundUserException() {
    super("Not found user", HttpStatus.BAD_REQUEST);
  }
}
