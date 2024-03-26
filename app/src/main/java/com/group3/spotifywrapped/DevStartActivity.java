package com.group3.spotifywrapped;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group3.spotifywrapped.SummaryActivity.TopSongsSummaryActivity;

import java.util.HashMap;
import java.util.Map;

public class DevStartActivity extends AppCompatActivity {
    Map<String, Button> buttons = new HashMap<String, Button>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dev_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_top_songs_summary), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttons.put("summary_activity_button", (Button)findViewById(R.id.summary_activity_button));
        buttons.get("summary_activity_button").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DevStartActivity.this, TopSongsSummaryActivity.class);
                startActivity(i);
            }
        });
        buttons.put("TestActivity", (Button)findViewById(R.id.testActivity));
        buttons.get("TestActivity").setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DevStartActivity.this, TestActivity.class);
                startActivity(i);
            }
        });
    }
}