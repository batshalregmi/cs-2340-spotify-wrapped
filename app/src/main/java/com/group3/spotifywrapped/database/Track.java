package com.group3.spotifywrapped.database;

import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Track extends SpotifyItem {
    private static final String TAG = "Track";

    public Track() {
        super(SpotifyItem.TRACK, null, null, null);
    }
    public Track(String spotifyId, String name, String imageUrl) {
        super(SpotifyItem.TRACK, spotifyId, name, imageUrl);
    }

    public static Track parseFromJSON(JSONObject src) {
        Track temp = new Track();
        try {
            temp.spotifyId = src.getString("id");
            temp.name = src.getString("name");
            return temp;
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse Track from JSON: " + e.toString());
        }
        return temp;
    }

    @Override
    public String toString() {
        return String.format("Track::%s", name);
    }
}
