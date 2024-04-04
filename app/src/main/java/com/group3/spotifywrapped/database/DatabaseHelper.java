package com.group3.spotifywrapped.database;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseHelper {
    private static MyDatabase db = null;

    public static void init(Context context) {
        db = MyDatabase.getInstance(context);
    }

    public static void addSummaryEntry(List<String> trackNames, List<String> artistNames, List<String> albumURLs, List<String> artistProfilePictureURLs) {

    }
    public static List<Track> getTracks(long summaryEntryId) {
        AtomicReference<List<Track>> result = new AtomicReference<>();
        Thread getTracksThread = new Thread(new Runnable() {
            @Override
            public void run() {
                result.set(db.myDatabaseDao().getTracksBySummaryEntry(summaryEntryId));
            }
        });
        getTracksThread.start();
        try {
            getTracksThread.join();
        } catch (Exception e) {
            Log.e("DatabaseHelper", e.toString());
        }
        return result.get();
    }
    public static List<Artist> getArtists(long summaryEntryId) {
        AtomicReference<List<Artist>> result = new AtomicReference<>();
        Thread getArtistsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                result.set(db.myDatabaseDao().getArtistsBySummaryEntry(summaryEntryId));
            }
        });
        getArtistsThread.start();
        try {
            getArtistsThread.join();
        } catch (Exception e) {
            Log.e("DatabaseHelper", e.toString());
        }
        return result.get();
    }

    private long genUniqueId(Set<Long> existingId) {
        Random rand = new Random();
        long result = rand.nextLong();
        while (existingId.contains(result)) {
            result = rand.nextLong();
        }
        return result;
    }
}
