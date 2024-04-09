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

public class Artist {
    private static final String TAG = "Artist";

    public String name;
    private String profilePictureUrl;
    private Drawable profilePicture;

    public Artist() {}
    public Artist(String name, String profilePictureUrl) {
        this.name = name;
        setProfilePictureUrl(profilePictureUrl);
    }

    public static Artist parseFromJSON(JSONObject src) throws Exception {
        Artist newArtist = new Artist();
        newArtist.name = src.getString("name");
        newArtist.profilePictureUrl = src.getJSONArray("images").getJSONObject(0).getString("url");
        return newArtist;
    }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
        try {
            this.profilePicture = SpotifyApiHelper.loadImageFromURL(profilePictureUrl);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }
    public Drawable getProfilePicture() { return profilePicture; }

    @Override
    public String toString() {
        return String.format("Artist::%s", name);
    }
}
