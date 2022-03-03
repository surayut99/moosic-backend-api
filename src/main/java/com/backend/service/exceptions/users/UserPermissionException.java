package com.backend.service.exceptions.users;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class UserPermissionException extends MoosicException {
  public UserPermissionException() {
    super("This user is not able to do proceed this action", HttpStatus.UNAUTHORIZED);
  }
}
