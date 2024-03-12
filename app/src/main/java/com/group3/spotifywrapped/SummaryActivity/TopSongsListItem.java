package com.group3.spotifywrapped.SummaryActivity;

public class TopSongsListItem {
    private String songName;
    private int image;

    public TopSongsListItem(String songName, int image) {
        this.songName = songName;
        this.image = image;
    }

    public String getSongName() { return songName; }
    public int getImage() { return image; }
}
