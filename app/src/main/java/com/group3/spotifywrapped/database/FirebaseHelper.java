package com.group3.spotifywrapped.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static void deleteUser(FirebaseUser userRef) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot it : snapshot.getChildren()) {
                    if (it.child("userUid").getValue(String.class).equals(userRef.getUid())) {
                        it.getRef().removeValue();
                    }
                }
                userRef.delete();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
    }

    public static DatabaseReference addSummaryEntry(SummaryEntry newEntry, DatabaseReference userRef) {
        DatabaseReference result = userRef.child("entries").push();
        result.setValue(newEntry);
        return result;
    }

    public static DatabaseReference addArtist(Artist artist, DatabaseReference entryRef) {
        DatabaseReference result = entryRef.child("artists").push();
        result.setValue(artist);
        return result;
    }

    public static DatabaseReference addTrack(Track track, DatabaseReference entryRef) {
        DatabaseReference result = entryRef.child("tracks").push();
        result.setValue(track);
        return result;
    }

    public static List<SummaryEntry> getEntryFromUser(DatabaseReference userRef) {
        List<SummaryEntry> result = new ArrayList<>();
        userRef.child("entries").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot it : snapshot.getChildren()) {
                    result.add(it.getValue(SummaryEntry.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read entries", error.toException());
            }
        });
        return result;
    }


    public static List<SpotifyItem> getArtistsFromEntry(DatabaseReference entryRef) {
        List<SpotifyItem> result = new ArrayList<>();
        entryRef.child("artists").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                result.clear();
                for (DataSnapshot it : snapshot.getChildren()) {
                    result.add(it.getValue(Artist.class));
                }
                Log.d(TAG, "artists found: " + result.toString());
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
                    result.add(it.getValue(Track.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read tracks", error.toException());
            }
        });
        return result;
    }
}
