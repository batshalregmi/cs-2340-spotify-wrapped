package com.group3.spotifywrapped.SummaryActivity;

import android.graphics.drawable.Drawable;

public class TopSongsListItem {
    private String songName;
    private Drawable image;

    public TopSongsListItem(String songName, Drawable image) {
        this.songName = songName;
        this.image = image;
    }

    public String getSongName() { return songName; }
    public Drawable getImage() { return image; }
}
