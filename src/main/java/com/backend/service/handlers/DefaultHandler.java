package com.backend.service.handlers;

import com.backend.service.models.responses.APIResponse;
import org.springframework.boot.context.properties.bind.BindException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DefaultHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  public ResponseEntity<APIResponse> handleIllegalData(RuntimeException err, WebRequest req) {
    APIResponse res = APIResponse.builder()
        .success(false)
        .message(err.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(value = BindException.class)
  public ResponseEntity<APIResponse> handleResolver(RuntimeException err) {
    APIResponse res = APIResponse.builder()
        .success(false)
        .message(err.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<APIResponse> handleNotFoundHandler(NoHandlerFoundException err) {
    APIResponse res = APIResponse.builder()
        .success(false)
        .message(err.getMessage())
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<APIResponse> handle(MethodArgumentNotValidException err) {
    Map<String, String> errors = new HashMap<>();

    err.getBindingResult().getFieldErrors().forEach(e -> {
      errors.put(e.getField(), e.getDefaultMessage());
    });

    APIResponse res = APIResponse.builder()
        .success(false)
        .message("Request data contains invalid field")
        .errors(errors)
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<APIResponse> handleEmptyRequestBody(HttpMessageNotReadableException err) {
    APIResponse res = APIResponse.builder()
        .success(false)
        .message("Body request is required")
        .build();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
  }
}
