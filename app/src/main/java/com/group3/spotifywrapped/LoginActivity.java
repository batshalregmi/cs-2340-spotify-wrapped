package com.group3.spotifywrapped;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

import database.AppDatabase;
import database.User;
import database.UserDao;
import com.google.android.material.textfield.TextInputEditText;


public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;
    Button createAccount;

    public AppDatabase db;
    public static UserDao userDao;
    public Thread thread;

    private void prompt(String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Comp init
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        createAccount = findViewById(R.id.signupText);

        // Database init
        //db = Room.databaseBuilder(getApplicationContext(),
                //AppDatabase.class, "local-database").build();
        //userDao = db.userDao();

        db = null;
        userDao = null;

        loginButton.setOnClickListener(new View.OnClickListener() {
            String dbPassword;

            @Override
            public void onClick(View view) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbPassword = userDao.getPassword(username.getText().toString().toLowerCase());
                    }
                });
                thread.start();

                if (password.getText().toString().equals(dbPassword)) {
                    password = (EditText) findViewById(R.id.password);
                    loginButton = findViewById(R.id.loginButton);
                    loginButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (password.getText().toString().equals(dbPassword)) {
                                prompt("Login Successful!");
                            } else {
                                prompt("Login Failed!");
                            }
                        }
                    });
                }
                
                createAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(LoginActivity.this, SignUpScreen.class);
                        startActivity(i);
                    }
                });
            }

        });
    }}
