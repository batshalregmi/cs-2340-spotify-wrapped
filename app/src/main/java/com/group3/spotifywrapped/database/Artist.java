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
import java.util.Comparator;

public class Artist extends SpotifyItem{
    private static final String TAG = "Artist";

    public Artist() {
        super(SpotifyItem.ARTIST, null, null, null);
    }
    public Artist(String spotifyId, String name, String imageUrl) {
        super(SpotifyItem.ARTIST, spotifyId, name, imageUrl);
    }

    public static Artist parseFromJSON(JSONObject src) {
        Artist temp = new Artist();
        try {
            temp.spotifyId = src.getString("id");
            temp.name = src.getString("name");
            temp.setImageUrl(src.getJSONArray("images").getJSONObject(0).getString("url"));
            return temp;
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse Artist from JSON: " + e.toString());
        }
        return temp;
    }

    @Override
    public String toString() {
        return String.format("Artist::%s", name);
    }
}
