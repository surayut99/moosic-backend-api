package com.backend.service.models.authentication;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenCredential {
  @SerializedName(value = "moosic_id")
  private UUID id;

  @SerializedName(value = "user_id")
  private String exUid;

  @SerializedName(value = "is_active")
  private boolean isActive;
}
