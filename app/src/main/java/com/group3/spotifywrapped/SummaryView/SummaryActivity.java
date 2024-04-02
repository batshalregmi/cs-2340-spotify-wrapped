package com.group3.spotifywrapped.SummaryView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private List<TopSongsListItem> topSongsList = new ArrayList<>();

    private class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView songNameView;
        private TextView songNumberView;
        private ImageView albumCoverView;

        public MyViewHolder(View itemView) {
            super(itemView);
            songNameView = itemView.findViewById(R.id.song_name_text_view);
            songNumberView = itemView.findViewById(R.id.song_number_text_view);
            albumCoverView = itemView.findViewById(R.id.album_cover_view);
        }

        public TextView getSongNameView() {
            return songNameView;
        }
        public TextView getSongNumberView() { return songNumberView; }
        public ImageView getCoverView() { return albumCoverView; }
    }
    private class MyAdapter extends RecyclerView.Adapter<SummaryActivity.MyViewHolder> {
        private Context context;
        private List<TopSongsListItem> items;

        public MyAdapter(Context context, List<TopSongsListItem> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public SummaryActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SummaryActivity.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SummaryActivity.MyViewHolder holder, int position) {
            holder.songNameView.setText(items.get(position).getSongName());
            holder.songNumberView.setText(Integer.toString(position + 1));
            holder.albumCoverView.setImageDrawable(items.get(position).getImage());
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

        loadTopSongsList();
    }

    private void loadTopSongsList() {
        if (LoginActivity.activeUser.sToken != null) {
            JSONObject topSongsResponse = SpotifyApiHelper.callSpotifyApi("/me/top/tracks?time_range=long_term&limit=5", LoginActivity.activeUser.sToken, "GET"); // assuming TestActivity already executed to load token/code
            try {
                JSONArray topSongs = topSongsResponse.getJSONArray("items");
                for (int i = 0; i < topSongs.length(); ++i) {
                    topSongsList.add(getTopSongsListItemFromJSON(topSongs.getJSONObject(i)));
                }
            } catch (Exception e) {
                Log.e("SummaryActivity", "Exception when parsing JSON response: " + e);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SummaryActivity.MyAdapter(getApplicationContext(), topSongsList));
    }

    private TopSongsListItem getTopSongsListItemFromJSON(JSONObject temp) {
        Drawable albumCover;
        String name;
        try {
            name = temp.getString("name");
            String albumCoverURL = temp
                    .getJSONObject("album")
                    .getJSONArray("images")
                    .getJSONObject(0)
                    .getString("url");
            albumCover = SpotifyApiHelper.loadImageFromURL(albumCoverURL);
        } catch (Exception e) {
            Log.d("SummaryActivity", e.toString());
            albumCover = Drawable.createFromPath("C:\\CodeProjects\\Android Studio\\Projects\\cs-2340-spotify-wrapped\\app\\src\\main\\res\\drawable\\test_album_cover.jpeg");
            name = "N/A";
        }
        return new TopSongsListItem(name, albumCover);
    }
}