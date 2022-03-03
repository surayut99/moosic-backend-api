package com.backend.service.exceptions.users;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class DuplicatedUsernameException extends MoosicException {
  public DuplicatedUsernameException() {
    super("Duplicated username can not be allowed to be created", HttpStatus.BAD_REQUEST);
  }
}
