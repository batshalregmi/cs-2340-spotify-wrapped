package com.group3.spotifywrapped.CoreAppViews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
        JSONObject userResponse = SpotifyApiHelper.callSpotifyApi("/me", LoginActivity.token, "GET");
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
        } else if (imageUrl == null) {
            Drawable image = null;
            try {
                image = SpotifyApiHelper.loadImageFromURL("https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePicture.setImageDrawable(image);
        }
        TextView username = findViewById(R.id.usernameUnderPicture);
        //username.setText(LoginActivity.activeUser.username);


        Button deleteButton = findViewById(R.id.delete_account);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button changeEmailButton = findViewById(R.id.change_Email_Button);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button changePasswordButton = findViewById(R.id.change_password);
        deleteButton.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseHelper.deleteUser(FirebaseAuth.getInstance().getCurrentUser());
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
//
//            //FirebaseHelper.deleteUser(LoginActivity.activeUser);
//            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
//            startActivity(intent);
        });

        changeEmailButton.setOnClickListener(v -> {
            // open dialog to change email

            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Change Email");
            builder.setMessage("Enter new email address");



            builder.create();

        });
    }
}
