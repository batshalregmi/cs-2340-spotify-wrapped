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
    public static final String CLIENT_ID = "f2a3c6dab588480cbf5f7f93302bd1fe";
    public static final String CLIENT_SECRET = "f85f0ee0436f4c1fb00979a41126bb0f";
    public static final String REDIRECT_URI = "spotifywrapped://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;
    private String mAccessToken = "BQCTGrOTBDJaqvxEvvt4vKpXuvFzV2bwJ4gBzxbCOIc5mTwQal9PrK7aVy-Uui69K-VdvNcI99tm6dW7ssy8S-ha4KQYf82uu8dHtN3msanjMx0JCG908dBH1QLrvE2RH5Q6feTqN9xTZlGaIHJa6dSRRyhfdmmgk7W25iOSlgGtPVvnypIduUBHsjieimFKLFCckC3FomFKKlFojZAdU1e2L2yNqJH8SkzTeVGmrhUyOd45NPD-otyQARNQpSB1FAM8yfuPQ2DyKhtf";

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

        Log.d("SummaryActivity", "Current token: " + this.mAccessToken);
        loadTopSongsList();

        ((Button)findViewById(R.id.getTokenButton)).setOnClickListener((v) -> {
            //this.mAccessToken = TestActivity.mAccessToken;
            //Log.d("SummaryActivity", "New token: " + mAccessToken);
            loadTopSongsList();
        });
    }

    private void loadTopSongsList() {
        topSongsList.add(new TopSongsListItem(
                "EARFQUAKE",
                R.drawable.test_album_cover
        ));

        //TODO should be getting token/code from User class but no global scope
        SpotifyApiHelper spotifyApiHelper = new SpotifyApiHelper();
        if (this.mAccessToken != null) {
            JSONObject topSongsResponse = spotifyApiHelper.callSpotifyApi("/me/top/tracks?time_range=long_term&limit=1", this.mAccessToken, "GET"); // assuming TestActivity already executed to load token/code
            try {
                JSONArray topSongs = topSongsResponse.getJSONArray("items");
                topSongsList.clear();
                for (int i = 0; i < topSongs.length(); ++i) {
                    JSONObject temp = topSongs.getJSONObject(i);
                    topSongsList.add(new TopSongsListItem(
                            temp.getString("name"),
                            R.drawable.test_album_cover
                    ));
                }
            } catch (Exception e) {
                Log.d("SummaryActivity", "Exception when parsing JSON response: " + e);
            }
        }

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), topSongsList));
    }
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(SummaryActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-top-read", "user-read-email", "user-read-private" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
        }
    }
}