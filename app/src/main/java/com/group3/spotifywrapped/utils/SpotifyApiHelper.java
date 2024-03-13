package com.group3.spotifywrapped.utils;

import android.net.Uri;
import android.util.Log;

import com.group3.spotifywrapped.TestActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

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
        return callSpotifyApi(endpoint, TestActivity.mAccessToken, TestActivity.mAccessCode, method);
    }

    public JSONObject callSpotifyApi(String endpoint, String accessToken, String accessCode, String method) {
        if (accessToken == null || accessCode == null || method == null || endpoint == null) {
            throw new IllegalArgumentException("Invalid arguments");
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1" + endpoint)
                .addHeader("Authorization", "Bearer " + accessToken)
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
                Log.d("SpotifyApiHelper", "Response: " + retValue);
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
}
