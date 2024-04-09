package com.group3.spotifywrapped.summary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.group3.spotifywrapped.CoreAppViews.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.database.SummaryEntry;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import java.time.LocalDateTime;

public class SummarySelectorActivity extends AppCompatActivity {
    private static final String TAG = "SummarySelectorActivity";
    private static DatabaseReference selectedSummaryEntry = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_selector);

        Button genSummaryButton = findViewById(R.id.generateNewWrappedButton);
        genSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.activeUser != null) {
                    SummaryEntry newEntry = new SummaryEntry(LocalDateTime.now().toString());
                    newEntry.getArtists().addAll(SpotifyApiHelper.getTopArtistList());
                    newEntry.getTracks().addAll(SpotifyApiHelper.getTopTrackList());
                    selectedSummaryEntry = FirebaseHelper.addSummaryEntry(newEntry, LoginActivity.activeUser.get());
                    Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                    startActivity(i);
                } else {
                    Log.w(TAG, "activeUser must not be null before generating new wrapped");
                }
            }
        });
    }

    public static DatabaseReference getSelectedSummaryEntry() {
        return selectedSummaryEntry;
    }
}