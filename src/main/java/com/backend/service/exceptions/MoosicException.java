package com.backend.service.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@Data
@EqualsAndHashCode(callSuper = true)
public class MoosicException extends RuntimeException {
  private HttpStatus status;
  private Object body;

  public MoosicException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public MoosicException(String message, Object body, HttpStatus status) {
    super(message);
    this.body = body;
    this.status = status;
  }
}
