package com.group3.spotifywrapped.SummaryActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.TestActivity;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SummaryActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "cd5187268d4a421cbfda59e5c697e429";
    public static final String REDIRECT_URI = "spotifywrapped://auth";
    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private List<TopSongsListItem> topSongsList = new ArrayList<>();

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameView;
        private TextView songNumberView;
        private ImageView albumCoverView;

        public MyViewHolder(View itemView) {
            super(itemView);
            songNameView = itemView.findViewById(R.id.songNameView);
            songNumberView = itemView.findViewById(R.id.songNumberView);
            albumCoverView = itemView.findViewById(R.id.albumCoverView);
        }

        public TextView getSongNameView() {
            return songNameView;
        }
        public TextView getSongNumberView() { return songNumberView; }
        public ImageView getCoverView() { return albumCoverView; }
    }
    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private Context context;
        private List<TopSongsListItem> items;

        public MyAdapter(Context context, List<TopSongsListItem> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.songNameView.setText(items.get(position).getSongName());
            holder.songNumberView.setText(Integer.toString(position + 1));
            holder.albumCoverView.setImageResource(items.get(position).getImage());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        loadTopSongsList();
    }

    private void loadTopSongsList() {
        //TODO should be getting token/code from User class but no global scope
        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper();
        Log.i("Dev", TestActivity.mAccessToken);
        JSONObject topSongsResponse = spotifyApiHelper.callSpotifyApi("/me/top/tracks?time_range=long_term&limit=1", "GET"); // assuming TestActivity already executed to load token/code
        try {
            JSONArray topSongs = topSongsResponse.getJSONArray("items");
            for (int i = 0; i < topSongs.length(); ++i) {
                JSONObject temp = topSongs.getJSONObject(i);
                topSongsList.add(new TopSongsListItem(
                        temp.getString("name"),
                        R.drawable.test_album_cover
                ));
            }
        } catch (Exception e) {
            Log.e("JSON", "Exception when parsing JSON response: " + e);
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), topSongsList));
    }
}