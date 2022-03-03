package com.backend.service.services.ai;

import com.backend.service.exceptions.MoosicException;
import com.backend.service.models.responses.ai.MoodResponse;
import com.backend.service.services.http.HttpRequestPost;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class ImageMoodService {
  @Value("${ai.image.api.key}")
  private String API_KEY;

  @Value("${ai.image.url}")
  private String URL;

  public MoodResponse analyzeImage(MultipartFile file) throws IOException, MoosicException {
    ContentBody contentBody = new InputStreamBody(
        new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename());
    HttpEntity entity = MultipartEntityBuilder
        .create()
        .addPart("image", contentBody)
        .build();

    HttpRequestPost requestPost = new HttpRequestPost(URL);
    requestPost.setHeader("api_key", API_KEY);
    requestPost.setEntity(entity);

    JSONObject response = requestPost.execute();
    Gson gson = new GsonBuilder().serializeNulls().create();

    return gson.fromJson(response.toString(), MoodResponse.class);
  }
}
