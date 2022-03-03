package com.backend.service.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;


@TypeDefs({
    @TypeDef(
        name = "string-array",
        typeClass = StringArrayType.class
    )
})

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@ToString
public class UserModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @Column(name = "display_name")
  private String displayName;

  private String username;

  private String email;

  private String bio;

  @Column(name = "photo_url")
  private String photoUrl;

  @Type(type = "string-array")
  @Column(columnDefinition = "text[]")
  private String[] genres;

  private String uid;

  @Column(name = "is_active")
  @Generated(GenerationTime.INSERT)
  private boolean isActive;

  @Column(name = "created_at")
  @Generated(GenerationTime.INSERT)
  private String createdAt;

  @Column(name = "updated_at")
  @Generated(GenerationTime.ALWAYS)
  private String updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    UserModel userModel = (UserModel) o;
    return id != null && Objects.equals(id, userModel.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
