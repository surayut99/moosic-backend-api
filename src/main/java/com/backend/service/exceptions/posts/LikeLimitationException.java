package com.backend.service.exceptions.posts;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class LikeLimitationException extends MoosicException {
  public LikeLimitationException() {
    super("User already liked this post", HttpStatus.BAD_REQUEST);
  }
}
