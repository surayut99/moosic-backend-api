package com.backend.service.services.spotify;

import com.backend.service.models.spotify.requests.SpotifyRecommendationQuery;
import com.backend.service.models.spotify.responses.SpotifyPlaylist;
import com.backend.service.models.spotify.responses.SpotifyRecommendation;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import org.springframework.stereotype.Service;

@Service
public class SpotifyService extends SpotifyMainService {
  public static final int START_OFFSET = 0;
  public static final int LIMIT_PLAYLIST = 2;
  public static final int LIMIT_TRACK = 5;

  public SpotifyService() {
    super();
  }

  public SpotifyPlaylist[] searchPlaylist(String query) {
    return super.searchPlaylist(query, START_OFFSET, LIMIT_PLAYLIST);
  }

  public SpotifyPlaylist[] spotifyServices(String query, int offset) {
    return super.searchPlaylist(query, offset, LIMIT_PLAYLIST);
  }

  public SpotifyPlaylist[] spotifyServices(String query, int offset, int limit) {
    return super.searchPlaylist(query, offset, limit);
  }

  public SpotifyTrack[] getTracksFromPlaylist(String playlistURL) {
    return super.getTracksFromPlaylist(playlistURL, START_OFFSET, LIMIT_TRACK);
  }

  public SpotifyTrack[] getTracksFromPlaylist(String playlistURL, int offset) {
    return super.getTracksFromPlaylist(playlistURL, offset, LIMIT_TRACK);
  }

  public SpotifyTrack[] getTracksFromPlaylist(String playlistURL, int offset, int limit) {
    return super.getTracksFromPlaylist(playlistURL, offset, limit);
  }

  public SpotifyTrack getTrackById(String id) {
    return super.getTrackById(id);
  }

  public SpotifyRecommendation getRecommendedTracks(SpotifyRecommendationQuery query) {
    return super.getRecommendedTracks(query);
  }

}
