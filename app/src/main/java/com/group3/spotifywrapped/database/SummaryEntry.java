package com.group3.spotifywrapped.database;

import androidx.room.Entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SummaryEntry {
    public LocalDateTime dateCreated;
    public String timeFrame;

    private List<Artist> artists = new ArrayList<>();
    private List<Track> tracks = new ArrayList<>();

    public SummaryEntry(String dateCreated) {
        this.dateCreated = LocalDateTime.parse(dateCreated);
    }

    public List<Artist> getArtists() { return artists; }
    public List<Track> getTracks() { return tracks; }
}
