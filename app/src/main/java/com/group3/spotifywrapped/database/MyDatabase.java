package com.group3.spotifywrapped.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, SummaryEntry.class, Artist.class, Track.class}, version = 5)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase INSTANCE;

    public abstract MyDatabaseDao myDatabaseDao();

    public static MyDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyDatabase.class, "local-database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }
}
