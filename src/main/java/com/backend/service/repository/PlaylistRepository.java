package com.backend.service.repository;

import com.backend.service.models.entities.PlaylistModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistModel, UUID> {
  List<PlaylistModel> findByPostId(UUID postId);

  boolean existsPlaylistModelByIdAndPostId(UUID playlistId, UUID postId);
}
