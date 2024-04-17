package com.group3.spotifywrapped.CoreAppViews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.database.User;
import com.group3.spotifywrapped.summary.SummarySelectorActivity;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";

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

    private static final int SIGN_IN_PROCESSING = 0;
    private static final int SIGN_IN_FAIL = 1;
    private static final int SIGN_IN_SUCCESS = 2;
    private AtomicInteger signInState = new AtomicInteger(SIGN_IN_PROCESSING);
    private AtomicBoolean tokenRecieved = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText usernameTextField = findViewById(R.id.username);
        EditText passwordTextField = findViewById(R.id.password);
        EditText confirmPasswordTextField = findViewById(R.id.confirmPassword);
        EditText emailTextField = findViewById(R.id.email);
        EditText nameTextField = findViewById(R.id.name);
        Button createAccountButton = findViewById(R.id.createAccount);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, String.format("Registering new user using\nemail: %s\npassword: %s", emailTextField.getText().toString(), passwordTextField.getText().toString()));
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailTextField.getText().toString(), passwordTextField.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User newUser = new User();
                                    newUser.userUid = FirebaseAuth.getInstance().getUid();
                                    newUser.username = usernameTextField.getText().toString();
                                    newUser.name = nameTextField.getText().toString();
                                    Log.d(TAG, "adding new user to database");
                                    LoginActivity.activeUser.set(FirebaseHelper.addUser(newUser));
                                    Log.d(TAG, "Finished adding new user to database");
                                    signInState.set(SIGN_IN_SUCCESS);
                                } else {
                                    Toast.makeText(SignUpActivity.this, "Failed to sign up", Toast.LENGTH_SHORT).show();
                                    signInState.set(SIGN_IN_FAIL);
                                }
                            }
                        });

                        while (signInState.get() == SIGN_IN_PROCESSING);
                        if (signInState.get() == SIGN_IN_SUCCESS) {
                            signInState.set(SIGN_IN_PROCESSING);
                            Log.d(TAG, "Getting token");
                            getToken();
                            while(!tokenRecieved.get());
                            Log.d(TAG, "Token recieved: " + LoginActivity.token);
                            Intent i = new Intent(SignUpActivity.this, SummarySelectorActivity.class);
                            startActivity(i);
                        }
                        signInState.set(SIGN_IN_PROCESSING);
                    }
                });
                thread.start();
            }
        });
    }

    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(SignUpActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(SCOPES)
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SignUpActivity.super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            //Log.d("LoginActivity", "Response: " + response.getAccessToken());
            LoginActivity.token = response.getAccessToken();
            tokenRecieved.set(true);
        }
    }
}
