package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import org.json.JSONObject;

@Entity
public class Track {
    @NonNull
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "summary_entry_id")
    public long summaryEntryId;

    @ColumnInfo(name = "rank")
    public int rank;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "album_url")
    public String albumUrl;

    @Ignore
    public Track(String name, String albumUrl) {
        this.name = name;
        this.albumUrl = albumUrl;
    }

    public Track(@NonNull long id, long summaryEntryId, int rank, String name, String albumUrl) {
        this.id = id;
        this.summaryEntryId = summaryEntryId;
        this.rank = rank;
        this.name = name;
        this.albumUrl = albumUrl;
    }

    public static Track parseFromJSON(JSONObject src) throws Exception {
        return new Track(
                src.getString("name"),
                src.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url")
        );
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
        return id == temp.id && summaryEntryId == temp.summaryEntryId && name.equals(temp.name);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
