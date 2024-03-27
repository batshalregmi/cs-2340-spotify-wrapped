package com.group3.spotifywrapped;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import database.AppDatabase;
import database.User;
import database.UserDao;

import com.group3.spotifywrapped.SummaryActivity.SummaryPagerActivity;
import com.group3.spotifywrapped.SummaryActivity.TopSongsSummaryActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "9e3e12a5d1424f068a20c6db49de005c";
    public static final String REDIRECT_URI = "spotifywrapped://auth";
    public static final String[] SCOPES = new String[] {
            "user-read-recently-played",
            "user-library-read",
            "user-read-email",
            "user-top-read"
    };

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    public static User activeUser;

    private UserDao userDao;
    private boolean tokenRecieved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "local-database")
                .fallbackToDestructiveMigration()
                .build();
        userDao = db.userDao();

        //addTestUser();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread loginThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        List<User> usersFound = userDao.findByLoginInfo(username.getText().toString(), password.getText().toString());
                        if (!usersFound.isEmpty()) {
                            activeUser = usersFound.get(0);
                            getToken();
                            while (!tokenRecieved);
                            Log.d("LoginActivity", "Token recieved: " + activeUser.sToken);
                            Intent i = new Intent(LoginActivity.this, SummaryPagerActivity.class);
                            startActivity(i);
                        } else {
                            Log.e("LoginActivity", "Login credentials invalid");
                        }
                    }
                });
                loginThread.start();
            }
        });
    }

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(LoginActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(SCOPES) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            Log.d("LoginActivity", "Response: " + response.getAccessToken());
            activeUser.sToken = response.getAccessToken();
            tokenRecieved = true;
        }
    }

    private void addTestUser() {
        User trentUser = new User(
                "0",
                "tdoiron0",
                "1234",
                "trentwdoiron@gmail.com",
                "Trent Doiron",
                null,
                null
        );
        Thread insertUserThread = new Thread(new Runnable() {
            @Override
            public void run() {
                userDao.insert(trentUser);
            }
        });
        insertUserThread.start();
    }
}
