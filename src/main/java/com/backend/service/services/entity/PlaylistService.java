package com.backend.service.services.entity;

import com.backend.service.exceptions.playlists.NotFoundPlaylistException;
import com.backend.service.models.entities.PlaylistModel;
import com.backend.service.models.entities.PostModel;
import com.backend.service.models.responses.imageanalyze.PlaylistAnalyzedResponse;
import com.backend.service.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class PlaylistService {
  @Autowired
  private PlaylistRepository playlistRepo;

  @Transactional
  public PlaylistModel create(PlaylistAnalyzedResponse playlistCreation, PostModel post) {
    PlaylistModel playlist = new PlaylistModel();

    playlist.setPost(post);
    playlist.setMood(playlistCreation.getMood());
    playlist.setKeyword(playlistCreation.getKeyword());
    playlist.setName(playlistCreation.getPlaylist().getName());
    playlist.setURL(playlistCreation.getPlaylist().getUrl());

    return playlistRepo.saveAndFlush(playlist);
  }

  public PlaylistModel udpate(PlaylistModel data, UUID id) {
    return data;
  }

  public PlaylistModel getById(UUID id) throws NotFoundPlaylistException {
    return playlistRepo.findById(id).orElseThrow(() -> new NotFoundPlaylistException(id.toString()));
  }

  public boolean isPlaylistedRelatedToPost(UUID playlistId, UUID postId) {
    return playlistRepo.existsPlaylistModelByIdAndPostId(playlistId, postId);
  }

  public List<PlaylistModel> getAll() {
    return playlistRepo.findAll();
  }

  public List<PlaylistModel> getByPostId(UUID postId) {
    return playlistRepo.findByPostId(postId);
  }
}
