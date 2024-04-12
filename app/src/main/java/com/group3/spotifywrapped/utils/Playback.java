package com.group3.spotifywrapped.utils;

import android.content.Context;

import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;

import com.group3.spotifywrapped.CoreAppViews.LoginActivity;

import org.json.JSONObject;

import java.util.List;

public class Playback {
    UserTrackHistory uth;
    ExoPlayer player;

    public Playback(Context context) {
        player = new ExoPlayer.Builder(context).build();
        uth = new UserTrackHistory();
    }

    public void playSong(String url) {
        player.stop();
        player.clearMediaItems();
        player.addMediaItem(MediaItem.fromUri(url));
        player.prepare();
        player.play();
    }

    private void initQueue() {
        uth.retrieveHistory();
        List<String> trackIDs = uth.getTrackIDs();

        // Makes a ton of API calls might need to limit this
        for (String trackID : trackIDs) {
            if (trackID != null) {
                player.addMediaItem(MediaItem.fromUri(getTrackURL(trackID)));
            }
        }
    }

    public String getTrackURL(String trackID) {
        try {
            JSONObject response = SpotifyApiHelper.callSpotifyApi("/tracks/" + trackID, LoginActivity.token, "GET");
            return response.getString("preview_url");
        } catch (Exception e) {
            return null;
        }
    }

     public void startMedia() {
         initQueue();
         player.prepare();
         player.play();
     }
}
