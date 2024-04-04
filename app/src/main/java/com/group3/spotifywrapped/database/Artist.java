package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONException;
import org.json.JSONObject;

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
    public Artist(String name, String profilePictureUrl) {
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }
    public Artist(@NonNull long id, long summaryEntryId, int rank, String name, String profilePictureUrl) {
        this.id = id;
        this.summaryEntryId = summaryEntryId;
        this.rank = rank;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }

    public static Artist parseFromJSON(JSONObject src) throws Exception {
        return new Artist(
                src.getString("name"),
                src.getJSONArray("images").getJSONObject(0).getString("url")
        );
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
        return (id == temp.id) && (name.equals(temp.name));
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
