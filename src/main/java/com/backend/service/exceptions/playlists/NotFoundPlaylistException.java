package com.backend.service.exceptions.playlists;

import com.backend.service.exceptions.MoosicException;
import org.springframework.http.HttpStatus;

public class NotFoundPlaylistException extends MoosicException {
  public NotFoundPlaylistException(String message) {
    super(String.format("Not found playlist id: %s", message), HttpStatus.BAD_REQUEST);
  }
}
