package com.group3.spotifywrapped.CoreAppViews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;

import com.group3.spotifywrapped.summary.SummarySelectorActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

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

    private AtomicBoolean tokenRecieved = new AtomicBoolean(false);

    public static AtomicReference<DatabaseReference> activeUser = new AtomicReference<>(null);
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        EditText usernameTextField = findViewById(R.id.username);
        EditText passwordTextField = findViewById(R.id.password);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwordTextField.getText().toString().length() < 6) {
                    Toast.makeText(LoginActivity.this, "Password must be 6 characters long", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signInWithEmailAndPassword(usernameTextField.getText().toString(), passwordTextField.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            activeUser = FirebaseHelper.getUserByFirebaseUser();
                        }
                    }
                });
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getToken();
                        while(!tokenRecieved.get());
                        Intent i = new Intent(LoginActivity.this, SummarySelectorActivity.class);
                        startActivity(i);
                    }
                });
                thread.start();
            }
        });

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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
            //Log.d("LoginActivity", "Response: " + response.getAccessToken());
            token = response.getAccessToken();
            tokenRecieved.set(true);
        }
    }
}
