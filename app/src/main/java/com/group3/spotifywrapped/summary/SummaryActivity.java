package com.group3.spotifywrapped.summary;

import android.annotation.SuppressLint;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.Artist;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.database.SpotifyItem;
import com.group3.spotifywrapped.database.Track;
import com.group3.spotifywrapped.utils.Playback;
import com.group3.spotifywrapped.utils.ElementObserver;
import com.group3.spotifywrapped.utils.LiveDataList;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SummaryActivity extends AppCompatActivity {
    private static final String TAG = "SummaryActivity";

    private SpotifyItemAdapter artistAdapter;
    private SpotifyItemAdapter trackAdapter;
    private Playback player;

    private final LiveDataList<SpotifyItem> artists = new LiveDataList<>();
    private final LiveDataList<SpotifyItem> tracks = new LiveDataList<>();

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
    private class SpotifyItemAdapter extends RecyclerView.Adapter<SummaryActivity.MyViewHolder> {
        private Context context;
        private List<SpotifyItem> items;

        public SpotifyItemAdapter(Context context, List<SpotifyItem> items) {
            this.context = context;
            this.items = items;
        }

        @NonNull
        @Override
        public SummaryActivity.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new SummaryActivity.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull SummaryActivity.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.songNameView.setText(items.get(position).name);
            holder.songNumberView.setText(Integer.toString(position + 1));
            holder.albumCoverView.setImageDrawable(items.get(position).getImage());

            try {
                holder.albumCoverView.setOnClickListener(new View.OnClickListener() {
                    Track track = (Track) items.get(position);
                    String previewUrl = player.getTrackURL(track.spotifyId);

                    @Override
                    public void onClick(View v) {
                        Log.d("TEST", "MSG: " + previewUrl);
                        player.playSong(previewUrl);
                    }
                });
            } catch (Exception e) {
                Log.d("Exception", e.getMessage());
            }
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

        player = new Playback(this);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SummarySelectorActivity.setSelectedSummaryEntry(null);
                Intent i = new Intent(SummaryActivity.this, SummarySelectorActivity.class);
                startActivity(i);
            }
        });

        RecyclerView artistRecyclerView = findViewById(R.id.top_artists_list);
        artistRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        artistAdapter = new SpotifyItemAdapter(getApplicationContext(), artists);
        artistRecyclerView.setAdapter(artistAdapter);

        RecyclerView trackRecyclerView = findViewById(R.id.top_tracks_list);
        trackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackAdapter = new SpotifyItemAdapter(getApplicationContext(), tracks);
        trackRecyclerView.setAdapter(trackAdapter);

        artists.addObserver(new ElementObserver() {
            @Override
            public void onChange() {
                artistAdapter.notifyDataSetChanged();
            }
        });

        tracks.addObserver(new ElementObserver() {
            @Override
            public void onChange() {
                trackAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        artists.clear();
        tracks.clear();
        FirebaseHelper.getArtistsFromEntry(SummarySelectorActivity.getSelectedSummaryEntry(), artists);
        FirebaseHelper.getTracksFromEntry(SummarySelectorActivity.getSelectedSummaryEntry(), tracks);
    }
}