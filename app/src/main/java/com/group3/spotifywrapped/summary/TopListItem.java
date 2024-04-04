package com.group3.spotifywrapped.summary;

import android.graphics.drawable.Drawable;

public class TopListItem {
    private String name;
    private Drawable image;

    public TopListItem(String name, Drawable image) {
        this.name = name;
        this.image = image;
    }

    public String getName() { return name; }
    public Drawable getImage() { return image; }
}
