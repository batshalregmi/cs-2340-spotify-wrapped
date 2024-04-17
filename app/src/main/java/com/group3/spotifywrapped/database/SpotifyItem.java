package com.group3.spotifywrapped.database;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONObject;

import java.io.IOException;

public class SpotifyItem {
    private static final String TAG = "SpotifyItem";
    public static final int ARTIST = 0;
    public static final int TRACK = 1;

    public int type;
    public String spotifyId;
    public String name;

    private String imageUrl;
    private Drawable image;

    public SpotifyItem() {}
    public SpotifyItem(int type, String spotifyId, String name, String imageUrl) {
        this.type = type;
        this.spotifyId = spotifyId;
        this.name = name;
        setImageUrl(imageUrl);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        loadImage();
    }
    public String getImageUrl() { return imageUrl; }
    public Drawable getImage() {
        if (image == null && imageUrl != null) {
            loadImage();
        }
        return image;
    }

    private boolean loadImage() {
        if (imageUrl == null) {
            //Log.w(TAG, "Must set image URL before loading image");
            return false;
        }
        try {
            image = SpotifyApiHelper.loadImageFromURL(imageUrl);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("SpotifyItem:%s", name);
    }
}
