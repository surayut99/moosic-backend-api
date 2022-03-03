package com.backend.service.models.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@ToString
@Table(name = "posts")
public class PostModel {
  @Id
  @GeneratedValue(generator = "UUID")
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserModel user;

  @Column(name = "img_url")
  private String imgURL;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<PlaylistModel> playlists;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<InteractionModel> interaction;

  @Transient
  private String img;

  @Formula("(select count(*) from interactions i where i.post_id=id)")
  private long likeCount;

  @OneToOne(mappedBy = "post")
  private ExposedPostModel exposedPost;

//  @Column(name = "created_at")
//  @Generated(GenerationTime.INSERT)
//  private String createdAt;
//
//  @Column(name = "updated_at")
//  @Generated(GenerationTime.ALWAYS)
//  private String updatedAt;

  public PostModel() {
    playlists = new ArrayList<>();
  }

  public void addPlaylist(PlaylistModel playlist) {
    playlists.add(playlist);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    PostModel postModel = (PostModel) o;
    return id != null && Objects.equals(id, postModel.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
