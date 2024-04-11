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
        //String username = LoginActivity.activeUser.username;
        String message = (String) welcomeMessage.getText();
        //welcomeMessage.setText(message.replace("{{USER}}", username));


    }
}
