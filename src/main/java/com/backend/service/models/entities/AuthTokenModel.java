package com.backend.service.models.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "user_auth_tokens")
public class AuthTokenModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "user_id")
  private UUID userId;

//    @Column(name = "token")
//    private String token;

  @Column(name = "access_token")
  private String accessToken;

  @Column(name = "refresh_token")
  private String refreshToken;

  @Column(name = "expired_at", columnDefinition = "timestamptz")
  private String expiredAt;

  @Column(name = "created_at", insertable = false, updatable = false)
  @Generated(GenerationTime.INSERT)
  private String createdAt;

  @Column(name = "updated_at", insertable = false, updatable = false)
  @Generated(GenerationTime.ALWAYS)
  private String updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    AuthTokenModel that = (AuthTokenModel) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
