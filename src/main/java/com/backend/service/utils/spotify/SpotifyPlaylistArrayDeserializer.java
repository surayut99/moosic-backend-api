package com.backend.service.utils.spotify;

import com.backend.service.models.spotify.responses.SpotifyPlaylist;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SpotifyPlaylistArrayDeserializer implements JsonDeserializer<SpotifyPlaylist[]> {
  @Override
  public SpotifyPlaylist[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    Gson gson = new GsonBuilder().registerTypeAdapter(SpotifyPlaylist.class, new SpotifyPlaylistDeserializer()).create();
    JsonObject response = jsonElement.getAsJsonObject();
    JsonArray playlistsJson = response.getAsJsonObject("playlists").getAsJsonArray("items");
    List<SpotifyPlaylist> spotifyPlaylists = new ArrayList<>();

    playlistsJson.forEach(element -> spotifyPlaylists.add(gson.fromJson(element, SpotifyPlaylist.class)));
    return spotifyPlaylists.toArray(new SpotifyPlaylist[0]);
  }
}
