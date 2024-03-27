package com.group3.spotifywrapped.utils;

import android.util.Log;

import com.group3.spotifywrapped.LoginActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserTrackHistory {
    private class TrackHistoryItem {
        public String id;
        public String name;
        public LocalDateTime playedAt;

        public String toString() {
            return String.format("{%s, %s}", name, playedAt.toString());
        }
    }

    private List<TrackHistoryItem> trackHistory = new ArrayList<>();

    public UserTrackHistory() {}

    public void retrieveHistory() {
        LocalDateTime currentMoment = LocalDateTime.now();
        LocalDateTime endPoint = currentMoment.minusYears(1);
        while (endPoint.compareTo(currentMoment) < 0) {
            Duration difference = Duration.between(endPoint, currentMoment);
            String millis = Long.toString(difference.toMillis());
            Log.d("UserTrackHistory", "Getting history since: " + endPoint + " / " + millis);
            JSONObject response = SpotifyApiHelper.callSpotifyApi("/me/player/recently-played?after=" + millis + "&limit=50", LoginActivity.activeUser.sToken, "GET");
            List<TrackHistoryItem> parsedResponse = parseTrackHistorySegment(response);
            if (parsedResponse.isEmpty() || parsedResponse.size() < 2) {
                break;
            }
            Log.d("UserTrackHistory", "Adding tracks to history: " + parsedResponse);
            trackHistory.addAll(parsedResponse);
            endPoint = endPoint.plusNanos(difference.toMillis() * 1000000);
        }
    }
    public void updateHistory() {

    }

    private List<TrackHistoryItem> parseTrackHistorySegment(JSONObject src) {
        List<TrackHistoryItem> result = new ArrayList<>();
        try {
            JSONArray tracks = src.getJSONArray("items");
            for (int i = 0; i < tracks.length(); ++i) {
                TrackHistoryItem newItem = new TrackHistoryItem();
                newItem.id = tracks.getJSONObject(i).getJSONObject("track").getString("id");
                newItem.name = tracks.getJSONObject(i).getJSONObject("track").getString("name");
                newItem.playedAt = LocalDateTime.parse(tracks.getJSONObject(i).getString("played_at").substring(0, 22));
                result.add(newItem);
            }
        } catch (Exception e) {
            Log.e("UserTrackHistory", e.toString());
        }

        return result;
    }

    public int size() {
        return trackHistory.size();
    }
    public String toString() {
        return trackHistory.toString();
    }
}
