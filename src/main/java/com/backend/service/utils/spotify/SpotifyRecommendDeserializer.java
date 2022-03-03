package com.backend.service.utils.spotify;

import com.backend.service.models.spotify.responses.SpotifyRecommendation;
import com.backend.service.models.spotify.responses.SpotifySeed;
import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SpotifyRecommendDeserializer implements JsonDeserializer<SpotifyRecommendation> {
  @Override
  public SpotifyRecommendation deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(SpotifyTrack.class, new SpotifyTrackDeserializer())
        .create();
    JsonObject jsonObject = jsonElement.getAsJsonObject();

    SpotifySeed[] spotifySeed = gson.fromJson(jsonObject.getAsJsonArray("seeds"), SpotifySeed[].class);
    List<SpotifyTrack> tracks = new ArrayList<>();

    jsonObject.get("tracks").getAsJsonArray().forEach(t -> tracks.add(gson.fromJson(t, SpotifyTrack.class)));

    return SpotifyRecommendation.builder()
        .seeds(spotifySeed)
        .tracks(tracks.toArray(new SpotifyTrack[0]))
        .build();
  }
}
