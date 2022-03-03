package com.backend.service.services.firebase;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.exceptions.auth.InvalidRefreshTokenException;
import com.backend.service.models.responses.auths.RefreshTokenResponse;
import com.backend.service.services.http.HttpRequestPost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleAPITokenService {
  private final Gson gson;
  @Value("${firebase.auth.url}")
  private String authUrl;
  @Value("${firebase.apiKey}")
  private String apiKey;

  public GoogleAPITokenService() {
    gson = new GsonBuilder().serializeNulls().create();
  }

  public RefreshTokenResponse refreshToken(String refreshToken) throws MoosicException {
    HttpRequestPost requestPost = new HttpRequestPost(String.format("%s?key=%s", authUrl, apiKey));
    List<NameValuePair> body = new ArrayList<>();
    body.add(new BasicNameValuePair("grant_type", "refresh_token"));
    body.add(new BasicNameValuePair("refresh_token", refreshToken));

    try {
      requestPost.setEntity(new UrlEncodedFormEntity(body));
      JSONObject response = requestPost.execute();

      return gson.fromJson(response.toString(), RefreshTokenResponse.class);
    } catch (IOException e) {
      throw new InvalidRefreshTokenException();
    }
  }
}
