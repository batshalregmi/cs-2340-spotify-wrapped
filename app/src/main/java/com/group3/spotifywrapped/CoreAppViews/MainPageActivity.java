package com.group3.spotifywrapped.CoreAppViews;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group3.spotifywrapped.R;

public class MainPageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postlogin);
        TextView welcomeMessage = findViewById(R.id.welcomeMessage);
        String username = LoginActivity.activeUser.username;
        String message = (String) welcomeMessage.getText();
        welcomeMessage.setText(message.replace("{{USER}}", username));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_wrapped:

                    break;

                case R.id.bottom_settings:
                    Intent intent1 = new Intent(MainPageActivity.this, SettingsActivity.class);
                    startActivity(intent1);
                    break;
            }


            return false;
        });

    }
}
