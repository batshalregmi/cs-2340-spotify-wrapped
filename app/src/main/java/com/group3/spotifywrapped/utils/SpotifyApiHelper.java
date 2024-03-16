package com.group3.spotifywrapped.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpotifyApiHelper {
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    Call mCall;
    JSONObject retValue = null;

    public JSONObject callSpotifyApi(String endpoint, String method) {
        return callSpotifyApi(endpoint, "", method);
    }

    public JSONObject callSpotifyApi(String endpoint, String accessToken, String method) {
        if (accessToken == null || method == null || endpoint == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1" + endpoint)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();
        Log.d("SpotifyApiHelper", "Request used: " + request);

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        final CountDownLatch latch = new CountDownLatch(1);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failure to fetch data: " + e);
                latch.countDown();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        retValue = new JSONObject(response.body().string());
                    } catch (JSONException e) {
                        cancelCall();
                        Log.d("JSON", "Failed to parse data: " + e);
                    }
                } else {
                    cancelCall();
                    Log.d("HTTP", "Failed to fetch data: " + response);
                }
                Log.d("SpotifyApiHelper", "Response: " + retValue);
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException e) {
            cancelCall();
            Log.d("HTTP", "Thread interrupted while waiting for response");
            Thread.currentThread().interrupt();
        }

        return retValue;
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }
}
