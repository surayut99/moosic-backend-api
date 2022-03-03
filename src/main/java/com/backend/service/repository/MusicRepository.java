package com.backend.service.repository;

import com.backend.service.models.entities.MusicModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MusicRepository extends JpaRepository<MusicModel, UUID> {
  //    List<MusicModel> findByAlbumCoverURL(String url);
  List<MusicModel> findByPlaylistId(UUID playlistId);

  boolean existsMusicModelByIdAndPlaylistId(UUID musicId, UUID playlistId);
}
