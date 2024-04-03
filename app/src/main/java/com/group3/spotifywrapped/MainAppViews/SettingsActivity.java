package com.group3.spotifywrapped.MainAppViews;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_wrapped:
                    Intent intent1 = new Intent(SettingsActivity.this, MainPageActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.bottom_settings:
                    break;
            }


            return false;
        });

        ImageView profilePicture = findViewById(R.id.userProfilePicture);
        JSONObject userResponse = SpotifyApiHelper.callSpotifyApi("/me", LoginActivity.activeUser.sToken, "GET");
        String imageUrl = null;
        Log.d("SettingsActivity", "User response: " + userResponse.toString());
        try {
            JSONArray images = userResponse.getJSONArray("images");
            Log.d("SettingsActivity", "User images: " + images.toString());
            if (images.length() > 0) {

                imageUrl = images.getJSONObject(images.length() - 1).getString("url");
                Log.d("SettingsActivity", "User image URL: " + imageUrl);

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (imageUrl != null) {
            Drawable image = null;
            try {
                image = SpotifyApiHelper.loadImageFromURL(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePicture.setImageDrawable(image);
        }
        TextView username = findViewById(R.id.usernameUnderPicture);
        username.setText(LoginActivity.activeUser.username);
    }
}
