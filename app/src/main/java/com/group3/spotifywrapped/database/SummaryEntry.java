package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SummaryEntry {
    @NonNull
    @PrimaryKey
    public long id;

    @ColumnInfo(name = "user_id")
    public long userId;

    @ColumnInfo(name = "date_created")
    public String dateCreated;

    public SummaryEntry(@NonNull long id, long userId, String dateCreated) {
        this.id = id;
        this.userId = userId;
        this.dateCreated = dateCreated;
    }
}
