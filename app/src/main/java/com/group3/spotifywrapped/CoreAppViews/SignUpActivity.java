package com.group3.spotifywrapped.CoreAppViews;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.DatabaseHelper;
import com.group3.spotifywrapped.database.User;


public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditText username = findViewById(R.id.username);
        EditText password = findViewById(R.id.password);
        EditText confirmPassword = findViewById(R.id.confirmPassword);
        EditText email = findViewById(R.id.email);
        EditText name = findViewById(R.id.name);
        Button createAccountButton = findViewById(R.id.createAccount);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity.mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                } else {
                                    Log.d(TAG, "createUserWithEmail: failure");
                                }
                            }
                        });
            }
        });
    }

    private void addNewUser(String username, String password, String name, String email) {
        User newUser = new User();
        newUser.username = username;
        newUser.password = password;
        newUser.name = name;
        newUser.email = email;
        DatabaseHelper.insertUser(newUser);
    }
}
