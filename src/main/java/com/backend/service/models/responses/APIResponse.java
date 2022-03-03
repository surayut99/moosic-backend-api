package com.backend.service.models.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class APIResponse {
  private boolean success;
  private String message;
  private Object data;
  private Object errors;

  public APIResponse(boolean success, String message, Object data) {
    this.success = success;
    this.message = message;
    this.data = data;
  }
}
