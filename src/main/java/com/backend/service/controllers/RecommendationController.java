package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.entities.ExposedPostModel;
import com.backend.service.models.entities.MusicModel;
import com.backend.service.models.entities.UserModel;
import com.backend.service.models.responses.APIResponse;
import com.backend.service.models.responses.recommedations.RecommendationResponse;
import com.backend.service.models.spotify.requests.SpotifyRecommendationQuery;
import com.backend.service.models.spotify.responses.SpotifyRecommendation;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.backend.service.services.entity.ExposedPostService;
import com.backend.service.services.spotify.SpotifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {
  @Autowired
  private ExposedPostService exposedPostService;
  @Autowired
  private SpotifyService spotifyService;

  @GetMapping("/top-posts")
  public ResponseEntity<APIResponse> getRecommendation(
      @RequestParam(defaultValue = "0") Integer page,
      HttpServletRequest request) throws MoosicException {

    UserModel user = (UserModel) request.getAttribute("user");
    List<ExposedPostModel> posts = exposedPostService.getTopPost().getContent();
    String[] genres = user.getGenres();

    if (genres == null || genres.length == 0) {
      genres = new String[]{"acoustic", "pop", "dance"};
    }

    int selectedSeed = page % 3;
    MusicModel music = posts.get(selectedSeed).getMusic();
    String selectedTrackId = music.getSpotifyId();
    SpotifyTrack track = spotifyService.getTrackById(selectedTrackId);
    String selectedArtistIds = String.join(",", track.getArtists());

    final int limit = 1;
    SpotifyRecommendationQuery query = SpotifyRecommendationQuery.builder()
        .seed_tracks(selectedTrackId)
        .seed_artists(selectedArtistIds)
        .seed_genres(String.join(",", genres))
        .limit(limit)
        .offset(page)
        .build();

    SpotifyRecommendation recommendation = spotifyService.getRecommendedTracks(query);
    APIResponse response = APIResponse.builder()
        .success(true)
        .message("Get Recommended tracks successfully")
        .data(RecommendationResponse.builder()
            .mood(music.getPlaylist().getMood())
            .keyword(music.getPlaylist().getKeyword())
            .tracks(recommendation.getTracks())
            .page(page)
            .count(recommendation.getTracks().length)
            .build())
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
