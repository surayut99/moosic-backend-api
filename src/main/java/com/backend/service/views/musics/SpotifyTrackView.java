package com.backend.service.views.musics;

import org.springframework.beans.factory.annotation.Value;

public interface SpotifyTrackView {

  @Value("#{target.getData().getTitle()}")
  String getTitle();

  @Value("#{target.getData().getArtists()}")
  String[] getArtists();

  @Value("#{target.getData().getAlbum()}")
  String getAlbum();

  @Value("#{target.getData().getTrackURL()}")
  String getTrackURL();

  @Value("#{target.getData().getArtistURLs()}")
  String[] getArtisURLs();

  @Value("#{target.getData().getAlbumURL()}")
  String getAlbumURL();

  @Value("#{target.getData().getPreviewURL()}")
  String getPreviewURL();

}
