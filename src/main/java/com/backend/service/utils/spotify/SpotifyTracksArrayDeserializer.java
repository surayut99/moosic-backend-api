package com.backend.service.utils.spotify;

import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SpotifyTracksArrayDeserializer implements JsonDeserializer<SpotifyTrack[]> {
  @Override
  public SpotifyTrack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    Gson gson = new GsonBuilder().registerTypeAdapter(SpotifyTrack.class, new SpotifyTrackDeserializer()).create();
    JsonArray objects = jsonElement.getAsJsonArray();
//        JsonArray objects = jsonElement.getAsJsonObject().getAsJsonArray("items");

    List<SpotifyTrack> tracks = new ArrayList<>();

    objects.forEach(obj -> {
      tracks.add(gson.fromJson(obj.getAsJsonObject().get("track").toString(), SpotifyTrack.class));
    });

    return tracks.toArray(new SpotifyTrack[0]);
  }
}
