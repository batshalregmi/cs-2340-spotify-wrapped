package com.group3.spotifywrapped.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

import com.group3.spotifywrapped.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyApiHelper {
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private Call mCall;
    private JSONObject retValue = null;

    public JSONObject callSpotifyApi(String endpoint, String method) {
        return callSpotifyApi(endpoint, LoginActivity.activeUser.sToken, method);
    }

    public JSONObject callSpotifyApi(String endpoint, String mAccessToken, String method) {
        Log.d("SpotifyApiHelper", "Making Spotify API call...");
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1" + endpoint)
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        mCall = mOkHttpClient.newCall(request);

        final CountDownLatch latch = new CountDownLatch(1);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject jsonObject = null;
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        jsonObject = new JSONObject(responseBody);
                    } catch (JSONException e) {
                        Log.d("JSON", "Failed to parse data: " + e);
                    }
                } else {
                    Log.d("HTTP", "Failed to fetch data: " + response);
                }
                retValue = jsonObject;
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            Log.d("HTTP", "Thread interrupted while waiting for response");
            Thread.currentThread().interrupt();
        }

        return retValue;
    }

    public static Drawable loadImageFromURL(String url) throws java.io.IOException {
        InputStream is = (InputStream)(new URL(url).getContent());
        return Drawable.createFromStream(is, "src name");
    }
}
