package com.group3.spotifywrapped.database;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.room.Database;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.group3.spotifywrapped.CoreAppViews.LoginActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseHelper {
    private static final String TAG = "FirebaseHelper";
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public static User getUserByFirebaseUser() {
        String Uid = FirebaseAuth.getInstance().getUid();
        AtomicReference<User> result = new AtomicReference<>(null);
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot it : snapshot.getChildren()) {
                    User curr = it.getValue(User.class);
                    if (curr.Uid.equals(Uid)) {
                        result.set(curr);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "loadPost:onCancelled", error.toException());
            }
        });
        return result.get();
    }

    public static void addUser(User newUser) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users");
        userRef.push().setValue(newUser);
    }
}
