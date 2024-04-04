package com.group3.spotifywrapped.summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.DatabaseHelper;
import com.group3.spotifywrapped.database.SummaryEntry;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import java.time.LocalDateTime;
import java.util.List;

public class SummarySelectorActivity extends AppCompatActivity {
    private static Long selectedEntryId = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_selector);

        List<SummaryEntry> summaryEntries = DatabaseHelper.getUserSummaryEntries(LoginActivity.activeUser.id);
        LinearLayout ll = findViewById(R.id.linearLayout);
        for (SummaryEntry it : summaryEntries) {
            Button btn = new Button(this);
            LocalDateTime date = LocalDateTime.parse(it.dateCreated);
            btn.setText(String.format("%s-%d-%d\n%d:%d", date.getMonthValue(), date.getDayOfMonth(), date.getYear(), date.getHour(), date.getMinute()));
            btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedEntryId = it.id;
                    Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                    startActivity(i);
                }
            });
            ll.addView(btn);
        }

        Button genSummaryButton = findViewById(R.id.generateNewWrappedButton);
        genSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedEntryId = DatabaseHelper.insertSummaryEntry(
                        SpotifyApiHelper.getTopArtistList(),
                        SpotifyApiHelper.getTopTrackList()
                );
                Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                startActivity(i);
            }
        });
    }

    public static long getSelectedEntryId() {
        return selectedEntryId;
    }
}