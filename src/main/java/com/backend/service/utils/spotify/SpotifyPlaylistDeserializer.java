package com.backend.service.utils.spotify;

import com.backend.service.models.spotify.responses.SpotifyPlaylist;
import com.google.gson.*;

import java.lang.reflect.Type;

public class SpotifyPlaylistDeserializer implements JsonDeserializer<SpotifyPlaylist> {
  @Override
  public SpotifyPlaylist deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    Gson gson = new Gson();
    SpotifyPlaylist playlist = gson.fromJson(jsonElement, SpotifyPlaylist.class);
    JsonObject object = jsonElement.getAsJsonObject();

    playlist.setTracksURL(object.getAsJsonObject("tracks").get("href").getAsString());
//        playlist.setTotal(object.getAsJsonObject("tracks").get("total").getAsInt());
    playlist.setExternalURL(object.getAsJsonObject("external_urls").get("spotify").getAsString());

    return playlist;
  }
}
