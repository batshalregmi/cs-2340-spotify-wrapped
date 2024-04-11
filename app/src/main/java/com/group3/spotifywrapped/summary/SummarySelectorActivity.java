package com.group3.spotifywrapped.summary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.group3.spotifywrapped.CoreAppViews.LoginActivity;
import com.group3.spotifywrapped.CoreAppViews.SignUpActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.database.SummaryEntry;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SummarySelectorActivity extends AppCompatActivity {
    private static final List<Pair<DatabaseReference, SummaryEntry>> entries = Collections.synchronizedList(new ArrayList<>());
    private static final List<String> entriesStringList = new ArrayList<>();
    private ArrayAdapter<String> allPreviousEntrySpinnerAdapter = null;
    public static AtomicBoolean foundEntries = new AtomicBoolean(false);

    private static final String TAG = "SummarySelectorActivity";
    private static DatabaseReference selectedSummaryEntry = null;
    private static final int MAX_TABLE_ROWS = 2;
    private static final int MAX_TABLE_COLUMNS = 4;

    private String selectedTimeRange = "short_term";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_selector);

        Spinner timeRangeSpinner = findViewById(R.id.timeRangeSpinner);
        ArrayAdapter<CharSequence> timeRangeSpinnerAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.time_frame_choices,
                android.R.layout.simple_spinner_item
        );
        timeRangeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeRangeSpinner.setAdapter(timeRangeSpinnerAdapter);

        timeRangeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((String)parent.getItemAtPosition(position)) {
                    case "1 year":
                        selectedTimeRange = "long_term";
                    case "1 month":
                        selectedTimeRange = "short_term";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button genSummaryButton = findViewById(R.id.generateNewWrappedButton);
        genSummaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LoginActivity.activeUser.get() != null) {
                    SummaryEntry newEntry = new SummaryEntry(LocalDateTime.now().toString());
                    newEntry.getArtists().addAll(SpotifyApiHelper.getTopArtistList(selectedTimeRange));
                    newEntry.getTracks().addAll(SpotifyApiHelper.getTopTrackList(selectedTimeRange));
                    selectedSummaryEntry = FirebaseHelper.addSummaryEntry(newEntry, LoginActivity.activeUser.get());
                    Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                    startActivity(i);
                } else {
                    Log.w(TAG, "activeUser must not be null before generating new wrapped");
                }
            }
        });

        Spinner allPreviousEntrySpinner = findViewById(R.id.allPreviousWrapSpinner);
        allPreviousEntrySpinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                entriesStringList
        );
        allPreviousEntrySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allPreviousEntrySpinner.setAdapter(allPreviousEntrySpinnerAdapter);
        allPreviousEntrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String target = (String)parent.getItemAtPosition(position);
                for (Pair<DatabaseReference, SummaryEntry> it : entries) {
                    if (target.equals(it.second.dateCreated.toString())) {
                        selectedSummaryEntry = it.first;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        Button openEntryButton = findViewById(R.id.openWrappedButton);
        openEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedSummaryEntry != null) {
                    Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(SummarySelectorActivity.this, "Must select an summary entry before opening one", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        Thread previousEntriesGridThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!foundEntries.get()) {
                    while (LoginActivity.activeUser.get() == null);
                    FirebaseHelper.getEntriesFromUser(LoginActivity.activeUser.get(), entries);
                    while (!foundEntries.get());
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Num entries found: " + entries.size());

                        entries.sort(new Comparator<Pair<DatabaseReference, SummaryEntry>>() {
                            @Override
                            public int compare(Pair<DatabaseReference, SummaryEntry> o1, Pair<DatabaseReference, SummaryEntry> o2) {
                                return o2.second.dateCreated.compareTo(o1.second.dateCreated);
                            }
                        });

                        TableLayout table = findViewById(R.id.recentWrapsTable);
                        table.removeAllViewsInLayout();

                        TableRow row = null;
                        for (int i = 0; i < MAX_TABLE_COLUMNS * MAX_TABLE_ROWS && i < entries.size(); ++i) {
                            if (i % MAX_TABLE_COLUMNS == 0) {
                                row = new TableRow(SummarySelectorActivity.this);
                                row.setGravity(Gravity.CENTER);
                                table.addView(row);
                            }

                            Button btn = new Button(SummarySelectorActivity.this);
                            LocalDateTime tempDate = entries.get(i).second.dateCreated;
                            btn.setText(String.format("%d-%d-%d\n%d:%02d %s", tempDate.getMonthValue(), tempDate.getDayOfMonth(), tempDate.getYear(), ((tempDate.getHour() == 12 || tempDate.getHour() == 0) ? 12 : tempDate.getHour() % 12), tempDate.getMinute(), ((tempDate.getHour() >= 12) ? "PM" : "AM")));
                            btn.setId(i);
                            final DatabaseReference ref = entries.get(i).first;
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectedSummaryEntry = ref;
                                    Log.d(TAG, "Num entries found: " + entries.size());
                                    Intent i = new Intent(SummarySelectorActivity.this, SummaryActivity.class);
                                    startActivity(i);
                                }
                            });
                            row.addView(btn);
                        }
                    }
                });

                entriesStringList.clear();
                for (Pair<DatabaseReference, SummaryEntry> it : entries) {
                    entriesStringList.add(it.second.dateCreated.toString());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        allPreviousEntrySpinnerAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        previousEntriesGridThread.start();
    }

    public static DatabaseReference getSelectedSummaryEntry() {
        return selectedSummaryEntry;
    }
    public static void setSelectedSummaryEntry(DatabaseReference ref) { selectedSummaryEntry = ref; }
}