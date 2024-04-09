package com.group3.spotifywrapped.database;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONObject;

import java.io.IOException;

public class Track {
    private static final String TAG = "Track";

    public String name;
    private String albumUrl;
    private Drawable albumImage;

    public Track() {}

    public static Track parseFromJSON(JSONObject src) throws Exception {
        Track newTrack = new Track();
        newTrack.name = src.getString("name");
        newTrack.albumUrl = src.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
        return newTrack;
    }

    public String getAlbumUrl() { return albumUrl; }
    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
        try {
            this.albumImage = SpotifyApiHelper.loadImageFromURL(albumUrl);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
    public Drawable getAlbumImage() { return albumImage; }

    @Override
    public String toString() {
        return String.format("Track::%s", name);
    }
}
