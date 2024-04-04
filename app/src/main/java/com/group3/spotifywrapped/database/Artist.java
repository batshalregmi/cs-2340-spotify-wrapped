package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

@Entity
public class Artist {
    @NonNull
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "summary_entry_id")
    public long summaryEntryId;

    @ColumnInfo(name = "rank")
    public int rank;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "profile_picture_url")
    public String profilePictureUrl;

    @Ignore
    public Artist() {}
    public Artist(@NonNull long id, long summaryEntryId, int rank, String name, String profilePictureUrl) {
        this.id = id;
        this.summaryEntryId = summaryEntryId;
        this.rank = rank;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }

    public static Artist parseFromJSON(JSONObject src) throws Exception {
        Artist newArtist = new Artist();
        newArtist.name = src.getString("name");
        newArtist.profilePictureUrl = src.getJSONArray("images").getJSONObject(0).getString("url");
        return newArtist;
    }

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
        return id == temp.id && summaryEntryId == temp.summaryEntryId && rank == temp.rank && name.equals(temp.name) && profilePictureUrl.equals(temp.profilePictureUrl);
    }
}
