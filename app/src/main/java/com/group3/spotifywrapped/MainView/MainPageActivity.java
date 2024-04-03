package com.group3.spotifywrapped.MainView;

import static com.group3.spotifywrapped.SignUpActivity.userDao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group3.spotifywrapped.LoginActivity;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.settingsActivity;

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
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottom_wrapped:

                        break;

                    case R.id.bottom_settings:
                        Intent intent1 = new Intent(MainPageActivity.this, settingsActivity.class);
                        startActivity(intent1);
                        break;

//                    case R.id.ic_books:
//                        Intent intent2 = new Intent(MainActivity.this, ActivityTwo.class);
//                        startActivity(intent2);
//                        break;
//
//                    case R.id.ic_center_focus:
//                        Intent intent3 = new Intent(MainActivity.this, ActivityThree.class);
//                        startActivity(intent3);
//                        break;
//
//                    case R.id.ic_backup:
//                        Intent intent4 = new Intent(MainActivity.this, ActivityFour.class);
//                        startActivity(intent4);
//                        break;
                }


                return false;
            }
        });

    }
}
