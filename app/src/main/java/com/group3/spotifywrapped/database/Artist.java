package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class Artist {
    private String name;
    private String profilePictureUrl;

    public Artist() {}
    public Artist(String name, String profilePictureUrl) {
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }

    public static Artist parseFromJSON(JSONObject src) throws Exception {
        Artist newArtist = new Artist();
        newArtist.name = src.getString("name");
        newArtist.profilePictureUrl = src.getJSONArray("images").getJSONObject(0).getString("url");
        return newArtist;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String toString() {
        return String.format("Artist::%s", name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !getClass().equals(o.getClass())) {
            return false;
        }
        Artist temp = (Artist)o;
        return name.equals(temp.name) && profilePictureUrl.equals(temp.profilePictureUrl);
    }
}
