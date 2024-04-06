package com.group3.spotifywrapped.summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.group3.spotifywrapped.CoreAppViews.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.DatabaseHelper;
import com.group3.spotifywrapped.database.SummaryEntry;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

public class SummarySelectorActivity extends AppCompatActivity {
    private static Long selectedEntryId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_selector);

        Button genSummaryButton = findViewById(R.id.generateNewWrappedButton);
        genSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                startActivity(i);
            }
        });
    }

    public static long getSelectedEntryId() {
        return selectedEntryId;
    }
}