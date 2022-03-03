package com.backend.service.handlers;

import com.backend.service.models.responses.APIResponse;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthorizationHandler {
  @ExceptionHandler(value = {FirebaseAuthException.class})
  public ResponseEntity<APIResponse> handleInvalidToken(FirebaseAuthException err) {
    APIResponse res = new APIResponse(false, err.getMessage(), null, null);
    return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
  }
}
