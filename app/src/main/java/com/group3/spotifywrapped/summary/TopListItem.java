package com.group3.spotifywrapped.summary;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import java.io.IOException;

public class TopListItem {
    private static final String TAG = "TopListItem";

    private String name;
    private String imageUrl;
    private Drawable image;

    public TopListItem() {}
    public TopListItem(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        try {
            this.image = SpotifyApiHelper.loadImageFromURL(this.imageUrl);
        } catch(IOException e) {
            Log.e(TAG, "Failed to load image from URL::" + e.toString());
        }
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Drawable getImage() { return image; }
    public void setImage(Drawable image) { this.image = image; }
}
