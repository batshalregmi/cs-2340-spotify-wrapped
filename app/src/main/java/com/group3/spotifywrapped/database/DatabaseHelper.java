package com.group3.spotifywrapped.database;

import android.content.Context;
import android.util.Log;

import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class DatabaseHelper {
    private enum GenUniqueIdMode{
        USER,
        SUMMARY_ENTRY,
        ARTIST,
        TRACK
    }

    private static MyDatabase db = null;

    public static void init(Context context) {
        db = MyDatabase.getInstance(context);
    }

    public static void addSummaryEntry(List<String> trackNames, List<String> artistNames, List<String> albumURLs, List<String> artistProfilePictureURLs) {
        long newEntryId = genUniqueId(GenUniqueIdMode.SUMMARY_ENTRY);
        SummaryEntry newEntry = new SummaryEntry(
                newEntryId,
                LoginActivity.activeUser.id,
                LocalDateTime.now().toString()
        );
        asyncInsertSummaryEntry(newEntry);
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

    private static long genUniqueId(GenUniqueIdMode mode) {
        AtomicReference<List<Long>> existingIdList = new AtomicReference<>();
        Runnable runnable;
        switch(mode) {
            case USER:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        existingIdList.set(db.myDatabaseDao().getUserIdList());
                    }
                };
                break;
            case SUMMARY_ENTRY:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        existingIdList.set(db.myDatabaseDao().getSummaryEntryIdList());
                    }
                };
                break;
            case ARTIST:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        existingIdList.set(db.myDatabaseDao().getArtistIdList());
                    }
                };
                break;
            case TRACK:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        existingIdList.set(db.myDatabaseDao().getTrackIdSet());
                    }
                };
                break;
            default:
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        existingIdList.set(new ArrayList<Long>());
                    }
                };
        }

        Thread getIdListThread = new Thread(runnable);
        getIdListThread.start();
        try {
            getIdListThread.join();
        } catch(Exception e) {
            Log.e("DatabaseHelper", e.toString());
        }

        Random rand = new Random();
        long result = rand.nextLong();
        while (existingIdList.get().contains(result)) {
            result = rand.nextLong();
        }
        return result;
    }
    private static void asyncInsertSummaryEntry(SummaryEntry newEntry) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.myDatabaseDao().insertSummaryEntry(newEntry);
            }
        }).start();
    }
    private static void asyncInsertArtist(Artist newArtist) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.myDatabaseDao().insertArtist(newArtist);
            }
        }).start();
    }
    private static void asyncInsertTrack(Track track) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.myDatabaseDao().insertTrack(track);
            }
        }).start();
    }
}
