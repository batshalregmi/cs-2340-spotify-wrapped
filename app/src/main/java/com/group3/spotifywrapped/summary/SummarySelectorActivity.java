package com.group3.spotifywrapped.summary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.SummaryEntry;

public class SummarySelectorActivity extends AppCompatActivity {
    private static SummaryEntry selectedEntry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_selector);

        Button genSummaryButton = findViewById(R.id.generateNewWrappedButton);
        genSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static SummaryEntry getSelectedEntry() {
        return selectedEntry;
    }
}