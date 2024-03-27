package com.group3.spotifywrapped;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
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


public class LoginActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    Button loginButton;

    public AppDatabase db;
    public static UserDao userDao;
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
            String dbPassword;
            String dbEmail;

            @Override
            public void onClick(View view) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbPassword = userDao.getPassword(username.getText().toString().toLowerCase());
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
                            if (username.getText().toString().equals("user") && password.getText().toString().equals("1234")) {
                                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                            System.out.println(password.getText().toString());
                            System.out.println(dbPassword);
                        }
                    });
                }
            }

            ;
        });
    }}
