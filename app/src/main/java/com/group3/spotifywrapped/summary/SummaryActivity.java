package com.group3.spotifywrapped.summary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.SignUpActivity;
import com.group3.spotifywrapped.database.Artist;
import com.group3.spotifywrapped.database.DatabaseHelper;
import com.group3.spotifywrapped.database.Track;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private List<TopListItem> topTracksList = new ArrayList<>();
    private List<TopListItem> topArtistsList = new ArrayList<>();

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
        private List<TopListItem> items;

        public MyAdapter(Context context, List<TopListItem> items) {
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
            holder.songNameView.setText(items.get(position).getName());
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

        loadTopArtistsList();
        loadTopTracksList();

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SummaryActivity.this, SummarySelectorActivity.class);
                startActivity(i);
            }
        });
    }

    private void loadTopArtistsList() {
        List<Artist> artists = DatabaseHelper.getArtists(SummarySelectorActivity.getSelectedEntryId());
        for (Artist it : artists) {
            try {
                topArtistsList.add(new TopListItem(it.name, SpotifyApiHelper.loadImageFromURL(it.profilePictureUrl)));
            } catch (Exception e) {
                Log.e("SummaryActivity", e.toString());
            }
        }

        RecyclerView recyclerView = findViewById(R.id.top_artists_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SummaryActivity.MyAdapter(getApplicationContext(), topArtistsList));
    }

    private void loadTopTracksList() {
        List<Track> tracks = DatabaseHelper.getTracks(SummarySelectorActivity.getSelectedEntryId());
        for (Track it : tracks) {
            try {
                topTracksList.add(new TopListItem(it.name, SpotifyApiHelper.loadImageFromURL(it.albumUrl)));
            } catch (Exception e) {
                Log.e("SummaryActivity", e.toString());
            }
        }

        RecyclerView recyclerView = findViewById(R.id.top_tracks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SummaryActivity.MyAdapter(getApplicationContext(), topTracksList));
    }
}