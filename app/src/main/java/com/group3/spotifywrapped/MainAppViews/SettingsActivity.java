package com.group3.spotifywrapped.MainAppViews;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

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
        Log.d("SettingsActivity", "User response: " + userResponse.toString());
    }
}
