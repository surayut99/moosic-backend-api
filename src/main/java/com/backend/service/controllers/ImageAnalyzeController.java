package com.backend.service.controllers;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.responses.APIResponse;
import com.backend.service.models.responses.ai.ImageContext;
import com.backend.service.models.responses.imageanalyze.ImageAnalyzeResponse;
import com.backend.service.models.responses.imageanalyze.PlaylistAnalyzedResponse;
import com.backend.service.models.spotify.responses.SpotifyPlaylist;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.backend.service.services.ai.ImageMoodService;
import com.backend.service.services.spotify.SpotifyService;
import com.backend.service.utils.FutureUtils;
import com.backend.service.utils.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RequestMapping("/image")
@RestController
public class ImageAnalyzeController {
  @Autowired
  private SpotifyService spotifyService;
  @Autowired
  private ImageMoodService imageMoodService;

  private final Logger logger = LoggerFactory.getLogger(ImageAnalyzeController.class);

  @PostMapping("/analyze")
  public ResponseEntity<APIResponse> analyzeImage(@RequestPart("image") MultipartFile file) throws MoosicException, ExecutionException, InterruptedException {
        ImageContext imageContext;

        try {
            imageContext = imageMoodService.analyzeImage(file).getData();
        } catch (IOException e) {
            throw new MoosicException("Something went wrong when analyzed image", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        String mood = imageContext.getMoods().size() == 0 ? "" : imageContext.getMoods().get(0);
        List<String> genres = Arrays.asList("pop", "blues") ;
        List<String> words = WordUtils.createCombinationWord(
                genres,
                mood,
                imageContext.getTime(),
                imageContext.getWeather()
                );

//    // fixed word for testing
//    List<String> words = Arrays.asList("chill pop", "happy rock", "rain country", "day happy");

    logger.info(words.toString());

    // get future instance
    FutureUtils futureUtils = FutureUtils.getInstance();

    // loop to create request for search playlist by keyword as async request
    List<CompletableFuture<List<PlaylistAnalyzedResponse>>> futures = words.stream()
        .map(keyword -> CompletableFuture.supplyAsync(() -> {
              // send query request to spotify
              SpotifyPlaylist[] playlists = spotifyService.searchPlaylist(keyword);

              // loop each playlist to create request to get tracks as async request
              List<CompletableFuture<PlaylistAnalyzedResponse>> playlistFutures = Arrays.stream(playlists)
                  .map(playlist -> CompletableFuture.supplyAsync(() -> {
                    // request to track of playlist
                    SpotifyTrack[] tracks = spotifyService.getTracksFromPlaylist(playlist.getTracksURL());

                    // build response playlist
                    return PlaylistAnalyzedResponse.builder()
                        .mood(keyword.split(" ")[0])
                        .keyword(keyword)
                        .playlist(playlist)
                        .tracks(tracks)
                        .build();
                  }))
                  .collect(Collectors.toList());

              // get result from futures
              return futureUtils.solveAllFutures(playlistFutures);
            })
        )
        .collect(Collectors.toList());

    // get playlists from each keyword, then flat list to 1D
    List<PlaylistAnalyzedResponse> playlistAnalyzedResponses = FutureUtils.getInstance()
        .solveAllFutures(futures)
        .stream().flatMap(Collection::stream)
        .collect(Collectors.toList());

    // build API response
    APIResponse response = APIResponse.builder()
        .success(true)
        .message("Analyze image mood successfully")
        .data(ImageAnalyzeResponse.builder()
            .keywords(words)
            .playlists(playlistAnalyzedResponses)
            .build()
        )
        .build();

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
