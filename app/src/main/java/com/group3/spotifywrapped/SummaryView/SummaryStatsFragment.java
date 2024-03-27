package com.group3.spotifywrapped.SummaryView;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;
import com.group3.spotifywrapped.utils.UserTrackHistory;

import org.json.JSONObject;

public class SummaryStatsFragment extends Fragment {
    public SummaryStatsFragment() {
        super(R.layout.fragment_summary_stats);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserTrackHistory history = new UserTrackHistory();
        history.genHistory();
        Log.d("SummaryStatsFragment", "Result: " + history.size() + ", " + history.toString());
    }
}