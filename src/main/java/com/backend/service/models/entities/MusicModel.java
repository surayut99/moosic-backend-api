package com.backend.service.models.entities;

import com.backend.service.models.spotify.responses.SpotifyTrack;
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
@Table(name = "musics")
public class MusicModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "playlist_id")
  private PlaylistModel playlist;

  @Column(name = "spotify_id")
  private String spotifyId;

  @Column(name = "created_at")
  @Generated(GenerationTime.INSERT)
  private String createdAt;

  @Column(name = "updated_at")
  @Generated(GenerationTime.ALWAYS)
  private String updatedAt;

  @Transient
  private SpotifyTrack data;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    MusicModel that = (MusicModel) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
