package com.backend.service.services.entity;

import com.backend.service.exceptions.musics.NotFoundMusicException;
import com.backend.service.models.entities.MusicModel;
import com.backend.service.models.entities.PlaylistModel;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.backend.service.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MusicService {
  @Autowired
  private MusicRepository musicRepo;

  public MusicModel create(SpotifyTrack track, PlaylistModel playlist) {
    MusicModel music = new MusicModel();

    music.setPlaylist(playlist);
    music.setSpotifyId(track.getId());

    return musicRepo.saveAndFlush(music);
  }

  public MusicModel update(MusicModel music) {
    return musicRepo.saveAndFlush(music);
  }

  public List<MusicModel> getAll() {
    return musicRepo.findAll();
  }

  public List<MusicModel> getByPlaylistId(UUID playlistId) {
    return musicRepo.findByPlaylistId(playlistId);
  }

  public void delete(UUID id) {
    musicRepo.deleteById(id);
  }

  public boolean isMusicRelatedToPlaylist(UUID musicId, UUID playlistId) {
    return musicRepo.existsMusicModelByIdAndPlaylistId(musicId, playlistId);
  }

  public MusicModel getById(UUID id) throws NotFoundMusicException {
    return musicRepo.findById(id).orElseThrow(() -> new NotFoundMusicException(id.toString()));
  }

//    public List<MusicModel> getAlbumCoverURL(String url) {
//        return musicRepo.findByAlbumCoverURL(url);
//    }
}
