package com.backend.service.services.spotify;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.spotify.requests.SpotifyRecommendationQuery;
import com.backend.service.models.spotify.responses.SpotifyError;
import com.backend.service.models.spotify.responses.SpotifyPlaylist;
import com.backend.service.models.spotify.responses.SpotifyRecommendation;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.backend.service.services.http.HttpRequestGet;
import com.backend.service.services.http.HttpRequestPost;
import com.backend.service.utils.QueryParamUtils;
import com.backend.service.utils.spotify.SpotifyPlaylistArrayDeserializer;
import com.backend.service.utils.spotify.SpotifyRecommendDeserializer;
import com.backend.service.utils.spotify.SpotifyTrackDeserializer;
import com.backend.service.utils.spotify.SpotifyTracksArrayDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class SpotifyMainService {
  @Value("${spotify.api.auth.url}")
  private String authURL;
  @Value("${spotify.api.client.id}")
  private String clientId;
  @Value("${spotify.api.client.secret}")
  private String clientSecret;

  private String tokenStr;
  private String accessToken;
  private Gson gson;

  @PostConstruct
  public void initializeAttributes() throws MoosicException {
    tokenStr = Base64.getEncoder().encodeToString(String.format("%s:%s", clientId, clientSecret).getBytes());
    gson = new GsonBuilder().serializeNulls().registerTypeAdapter(SpotifyPlaylist[].class, new SpotifyPlaylistArrayDeserializer()).registerTypeAdapter(SpotifyTrack[].class, new SpotifyTracksArrayDeserializer()).registerTypeAdapter(SpotifyRecommendation.class, new SpotifyRecommendDeserializer()).registerTypeAdapter(SpotifyTrack.class, new SpotifyTrackDeserializer()).create();
    setAccessToken();
  }

  private void setAccessToken() throws MoosicException {
    HttpRequestPost request = new HttpRequestPost(authURL);
    List<NameValuePair> body = new ArrayList<>();
    body.add(new BasicNameValuePair("grant_type", "client_credentials"));
    request.setHeader("Authorization", String.format("Basic %s", tokenStr));

    try {
      request.setEntity(new UrlEncodedFormEntity(body));
      accessToken = request.execute().get("access_token").toString();

//      accessToken = "BQCsg94YMcXc6IT_nPnnphHBoxQ9RVZ10WYRAOlhHGF-G7u00xlJu6p9joEUGihc0ltzdTQ2f7rf7H5Pmd4";
    } catch (IOException e) {
      throw new MoosicException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  private boolean isTokenExpiredError(JSONObject object) {
    if (object.keySet().contains("error")) {
      SpotifyError error = gson.fromJson(object.get("error").toString(), SpotifyError.class);

      return error.getMessage().contains("expired");
    }

    return false;
  }

  public JSONObject requestGet(String url) {
    HttpRequestGet request = new HttpRequestGet(url);
    request.addHeader("Authorization", String.format("Bearer %s", accessToken));

    try {
      JSONObject response = request.execute();

      // if content contains error due to token expired, go to reset token then call this method again
      if (isTokenExpiredError(response)) {
        setAccessToken();
        return requestGet(url);
      }

      return response;

    } catch (IOException e) {
      throw new MoosicException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public SpotifyPlaylist[] searchPlaylist(String query, int offset, int limit) {
    String API_URL = "https://api.spotify.com/v1/search";
    String url = String.format("%s?q=%s&type=playlist&offset=0&limit=%d", API_URL, query.replace(" ", "%20"), limit);
    JSONObject response = requestGet(url);

    return gson.fromJson(response.toString(), SpotifyPlaylist[].class);
  }

  public SpotifyTrack[] getTracksFromPlaylist(String playlistURL, int offset, int limit) {
    String url = String.format("%s?offset=0&limit=%d", playlistURL, limit);
    JSONObject response = requestGet(url);

    return gson.fromJson(response.getJSONArray("items").toString(), SpotifyTrack[].class);
  }

  public SpotifyRecommendation getRecommendedTracks(SpotifyRecommendationQuery queryParams) {
    String API_URL = "https://api.spotify.com/v1/recommendations";
    List<BasicNameValuePair> pairs = QueryParamUtils.getInstance().convertToNameValuePair(queryParams);
    String API_QUERY_PARAM = QueryParamUtils.getInstance().convertToString(pairs);
    JSONObject response = requestGet(String.format("%s%s", API_URL, API_QUERY_PARAM));

    return gson.fromJson(response.toString(), SpotifyRecommendation.class);
  }

  public SpotifyTrack getTrackById(String trackId) {
    String API_URL = "https://api.spotify.com/v1/tracks";
    JSONObject response = requestGet(String.format("%s/%s", API_URL, trackId));

    return gson.fromJson(response.toString(), SpotifyTrack.class);
  }

  public JSONObject getPlaylist(String url) {
    return requestGet(url);
  }
}