package com.group3.spotifywrapped.summary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.Artist;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.database.Track;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SummaryActivity extends AppCompatActivity {
    private AtomicBoolean continueUpdate = new AtomicBoolean(true);

    private MyArtistAdapter artistAdapter;
    private MyTrackAdapter trackAdapter;

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
    }
    private class MyArtistAdapter extends RecyclerView.Adapter<SummaryActivity.MyViewHolder> {
        private Context context;
        private List<Artist> items;

        public MyArtistAdapter(Context context, List<Artist> items) {
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
            holder.songNameView.setText(items.get(position).name);
            holder.songNumberView.setText(Integer.toString(position + 1));
            holder.albumCoverView.setImageDrawable(items.get(position).getProfilePicture());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    private class MyTrackAdapter extends RecyclerView.Adapter<SummaryActivity.MyViewHolder> {
        private Context context;
        private List<Track> items;

        public MyTrackAdapter(Context context, List<Track> items) {
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
            holder.songNameView.setText(items.get(position).name);
            holder.songNumberView.setText(Integer.toString(position + 1));
            holder.albumCoverView.setImageDrawable(items.get(position).getAlbumImage());
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

        RecyclerView artistRecyclerView = findViewById(R.id.top_artists_list);
        artistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistAdapter = new MyArtistAdapter(getApplicationContext(), FirebaseHelper.getArtistsFromEntry(SummarySelectorActivity.getSelectedSummaryEntry()));
        artistRecyclerView.setAdapter(artistAdapter);

        RecyclerView trackRecyclerView = findViewById(R.id.top_tracks_list);
        trackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackAdapter = new MyTrackAdapter(getApplicationContext(), FirebaseHelper.getTracksFromEntry(SummarySelectorActivity.getSelectedSummaryEntry()));
        trackRecyclerView.setAdapter(trackAdapter);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistAdapter.notifyDataSetChanged();
                trackAdapter.notifyDataSetChanged();

                //Intent i = new Intent(SummaryActivity.this, SummarySelectorActivity.class);
                //startActivity(i);
            }
        });
    }
}