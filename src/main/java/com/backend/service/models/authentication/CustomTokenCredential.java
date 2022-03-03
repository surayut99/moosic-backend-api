package com.backend.service.models.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomTokenCredential {
  private String uid;
  private ClaimCredential claims;
}
