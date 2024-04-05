package com.group3.spotifywrapped;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.group3.spotifywrapped.database.DatabaseHelper;
import com.group3.spotifywrapped.database.User;
import com.group3.spotifywrapped.summary.SummarySelectorActivity;


public class SignUpActivity extends AppCompatActivity {
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
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //dbUsername = userDao.getUsername(username.getText().toString().toLowerCase());
                    }
                });
                thread.start();

                if (DatabaseHelper.usernameExists(username.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "The Username has been taken!", Toast.LENGTH_SHORT).show();
                }
                if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Your passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    addNewUser(username.getText().toString(), password.getText().toString(), name.getText().toString(), email.getText().toString());
                    Toast.makeText(SignUpActivity.this, "New Account Created!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(i);
                }
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
