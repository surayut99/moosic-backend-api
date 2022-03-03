package com.backend.service.utils.spotify;

import com.backend.service.models.spotify.responses.SpotifyTrack;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SpotifyTrackDeserializer implements JsonDeserializer<SpotifyTrack> {
  @Override
  public SpotifyTrack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
//        JsonObject object = jsonElement.getAsJsonObject().getAsJsonObject("track");
    JsonObject object = jsonElement.getAsJsonObject();
    Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    SpotifyTrack track = gson.fromJson(object, SpotifyTrack.class);

    JsonObject albumObject = object.getAsJsonObject("album");
    JsonArray artistsObject = object.getAsJsonArray("artists");

    track.setTrackURL(object.getAsJsonObject("external_urls").get("spotify").getAsString());
    track.setAlbum(albumObject.get("name").getAsString());
    track.setAlbumCoverURL(albumObject
        .getAsJsonArray("images")
        .get(0)
        .getAsJsonObject()
        .get("url")
        .getAsString());
    track.setAlbumURL(albumObject.getAsJsonObject("external_urls").get("spotify").getAsString());

    List<String> artists = new ArrayList<>();
    List<String> artistsURL = new ArrayList<>();

    artistsObject.forEach(artist -> {
      artists.add(artist.getAsJsonObject().get("name").getAsString());
      artistsURL.add(artist.getAsJsonObject().getAsJsonObject("external_urls").get("spotify").getAsString());
    });

    track.setArtists(artists.toArray(new String[0]));
    track.setArtistURLs(artistsURL.toArray(new String[0]));

    return track;
  }
}
