package com.backend.service.handlers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.responses.APIResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppException {
  private final Logger logger = LoggerFactory.getLogger(AppException.class);

  @ExceptionHandler(value = {MoosicException.class})
  public ResponseEntity<APIResponse> handleMoosicError(MoosicException err) {
    APIResponse res = APIResponse.builder()
        .success(false)
        .message(err.getMessage())
        .errors(err.getBody())
        .build();

    return ResponseEntity.status(err.getStatus()).body(res);
  }

  @ExceptionHandler(value = {RuntimeException.class})
  public ResponseEntity<APIResponse> handleRuntimeException(RuntimeException err) {
    String message = err.getMessage();

    if (err.getMessage() == null) {
      message = "Something went wrong. Some value may be empty";
      logger.error(err.getMessage(), err);
    }

    APIResponse res = new APIResponse(false, message, null, null);
    return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
