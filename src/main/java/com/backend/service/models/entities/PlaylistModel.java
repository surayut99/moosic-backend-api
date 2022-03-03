package com.backend.service.models.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "playlists")
public class PlaylistModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private PostModel post;

  @OneToMany(mappedBy = "playlist", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<MusicModel> musics;

  private String name;
  private String mood;

  @Column(name = "url")
  private String URL;
  private String keyword;

  @Column(name = "created_at")
  @Generated(GenerationTime.INSERT)
  private String createdAt;

  @Column(name = "updated_at")
  @Generated(GenerationTime.ALWAYS)
  private String updatedAt;

  public PlaylistModel() {
    musics = new ArrayList<>();
  }

  public void addMusic(MusicModel music) {
    musics.add(music);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    PlaylistModel that = (PlaylistModel) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
