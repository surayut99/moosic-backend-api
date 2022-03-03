package com.backend.service.exceptions.interactions;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundInteractionException extends MoosicException {
  public NotFoundInteractionException() {
    super("Not found the interaction on this post", HttpStatus.BAD_REQUEST);
  }
}
