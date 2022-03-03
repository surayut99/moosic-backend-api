package com.backend.service.exceptions.musics;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundMusicException extends MoosicException {
  public NotFoundMusicException(String message) {
    super(String.format("Not found music id: %s related to this post", message), HttpStatus.BAD_REQUEST);
  }
}
