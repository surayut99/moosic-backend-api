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
@Table(name = "exposed_posts")
public class ExposedPostModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "post_id", referencedColumnName = "id")
  private PostModel post;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "playlist_id", referencedColumnName = "id")
  private PlaylistModel playlist;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "music_id", referencedColumnName = "id")
  private MusicModel music;

  @Transient
  private boolean isLiked;

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
    ExposedPostModel exposedPostModel = (ExposedPostModel) o;
    return id != null && Objects.equals(id, exposedPostModel.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
