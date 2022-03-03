package com.backend.service.handlers;

import com.backend.service.models.responses.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class DatasourceHandler {
  @ExceptionHandler({SQLException.class})
  public ResponseEntity<APIResponse> handlePostgreSQLError(SQLException err) {
    APIResponse res = new APIResponse(false, "SQL error occurred when interact with database", null, err.getMessage());

    return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
