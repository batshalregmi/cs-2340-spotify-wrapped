package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

public class Track {
    public String name;
    public String albumUrl;

    public Track() {}

    public Track(String name, String albumUrl) {
        this.name = name;
        this.albumUrl = albumUrl;
    }

    public static Track parseFromJSON(JSONObject src) throws Exception {
        Track newTrack = new Track();
        newTrack.name = src.getString("name");
        newTrack.albumUrl = src.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");
        return newTrack;
    }

    @Override
    public String toString() {
        return String.format("Track::%s", name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        Track temp = (Track)o;
        return name.equals(temp.name) && albumUrl.equals(temp.albumUrl);
    }
}
