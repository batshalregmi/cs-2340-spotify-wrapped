package com.group3.spotifywrapped.database;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group3.spotifywrapped.summary.SummaryActivity;
import com.group3.spotifywrapped.summary.SummarySelectorActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";

    public static AtomicReference<DatabaseReference> getUserByFirebaseUser() {
        String Uid = FirebaseAuth.getInstance().getUid();
        AtomicReference<DatabaseReference> result = new AtomicReference<>(null);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot it : snapshot.getChildren()) {
                    if (it.child("userUid").getValue(String.class).equals(Uid)) {
                        result.set(it.getRef());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });

        return result;
    }

    public static DatabaseReference addUser(User newUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        DatabaseReference result = userRef.push();
        result.setValue(newUser);
        return result;
    }

    public static DatabaseReference addSummaryEntry(SummaryEntry newEntry, DatabaseReference userRef) {
        DatabaseReference result = userRef.child("entries").push();
        result.child("dateCreated").setValue(newEntry.dateCreated.toString());
        for (Artist it : newEntry.getArtists()) {
            addArtist(it, result);
        }
        for (Track it : newEntry.getTracks()) {
            addTrack(it, result);
        }

        return result;
    }

    public static DatabaseReference addArtist(Artist artist, DatabaseReference entryRef) {
        DatabaseReference result = entryRef.child("artists").push();
        result.child("spotifyId").setValue(artist.spotifyId);
        result.child("name").setValue(artist.name);
        result.child("imageUrl").setValue(artist.getImageUrl());
        return result;
    }

    public static DatabaseReference addTrack(Track track, DatabaseReference entryRef) {
        DatabaseReference result = entryRef.child("tracks").push();
        result.child("spotifyId").setValue(track.spotifyId);
        result.child("name").setValue(track.name);
        result.child("imageUrl").setValue(track.getImageUrl());
        return result;
    }

    public static void getEntriesFromUser(DatabaseReference userRef, List<Pair<DatabaseReference, SummaryEntry>> result) {
        userRef.child("entries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Updating entry list");
                result.clear();
                for (DataSnapshot it : snapshot.getChildren()) {
                    result.add(new Pair<DatabaseReference, SummaryEntry>(it.getRef(), new SummaryEntry(it.child("dateCreated").getValue(String.class))));
                }
                SummarySelectorActivity.foundEntries.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to load summary entry list", error.toException());
            }
        });
    }

    public static List<SpotifyItem> getArtistsFromEntry(DatabaseReference entryRef) {
        List<SpotifyItem> result = new ArrayList<>();
        entryRef.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot it : snapshot.getChildren()) {
                    Artist newArtist = new Artist(
                            it.child("spotifyId").getValue(String.class),
                            it.child("name").getValue(String.class),
                            it.child("imageUrl").getValue(String.class)
                    );
                    result.add(newArtist);
                }
                SummaryActivity.foundArtists.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read artists", error.toException());
            }
        });
        return result;
    }

    public static List<SpotifyItem> getTracksFromEntry(DatabaseReference entryRef) {
        List<SpotifyItem> result = new ArrayList<>();
        entryRef.child("tracks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot it : snapshot.getChildren()) {
                    Track newTrack = new Track(
                            it.child("spotifyId").getValue(String.class),
                            it.child("name").getValue(String.class),
                            it.child("imageUrl").getValue(String.class)
                    );
                    result.add(newTrack);
                }
                SummaryActivity.foundTracks.set(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read tracks", error.toException());
            }
        });
        return result;
    }
}
