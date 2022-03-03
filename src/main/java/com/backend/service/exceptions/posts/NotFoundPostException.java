package com.backend.service.exceptions.posts;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class NotFoundPostException extends MoosicException {
  public NotFoundPostException(UUID id) {
    super("Not found post with id: " + id, HttpStatus.BAD_REQUEST);
  }
}
