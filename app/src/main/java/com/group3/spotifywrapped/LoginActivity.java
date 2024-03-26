package com.group3.spotifywrapped;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;

import database.AppDatabase;
import database.User;
import database.UserDao;
import com.google.android.material.textfield.TextInputEditText;
import com.group3.spotifywrapped.SummaryActivity.TopSongsSummaryActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;


public class LoginActivity extends AppCompatActivity {
    public static final String CLIENT_ID = "cd5187268d4a421cbfda59e5c697e429";
    public static final String REDIRECT_URI = "spotifywrapped://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private String mAccessToken;

    EditText username;
    EditText password;
    Button loginButton;

    public AppDatabase db;
    public static UserDao userDao;
    public static User mUser;
    public Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "local-database").build();
        userDao = db.userDao();

        loginButton.setOnClickListener(new View.OnClickListener() {
            String dbUsername;
            String dbEmail;
            String dbPassword;

            @Override
            public void onClick(View view) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbPassword = userDao.getPassword(username.getText().toString().toLowerCase());
                        dbUsername = userDao.getUsername(username.getText().toString().toLowerCase());
                    }
                });
                thread.start();

                // TODO: This is kind of buggy, will fix tonight
                // Only works with emails with the password "password"
                if (password.getText().toString().equals(dbPassword)) {
                    password = (EditText) findViewById(R.id.password);
                    loginButton = findViewById(R.id.loginButton);
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (username.getText().toString().equals(dbUsername) && password.getText().toString().equals(dbPassword)) {
                                getUser(username.getText().toString());
                                Intent i = new Intent(LoginActivity.this, TopSongsSummaryActivity.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                            System.out.println(password.getText().toString());
                            System.out.println(dbPassword);
                        }
                    });
                }
            }
        });
    }
    private void getUser(String username) {
        mUser.username = username;
        mUser.password = userDao.getPassword(username.toLowerCase());
        getToken();
        mUser.sToken = mAccessToken;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
        }
    }

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(LoginActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email", "user-top-read"}) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }
}
