package com.group3.spotifywrapped;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import database.AppDatabase;
import database.User;
import database.UserDao;


public class SignUpScreen extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirmPassword;
    EditText email;
    EditText name;
    Button createAccountButton;
    public static UserDao userDao;
    public AppDatabase db;
    public Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        createAccountButton = findViewById(R.id.createAccount);


        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "local-database").build();
        userDao = db.userDao();

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            String dbUsername;

            @Override
            public void onClick(View view) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dbUsername = userDao.getUsername(username.getText().toString().toLowerCase());
                    }
                });
                thread.start();

                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(SignUpScreen.this, "Your passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    // stoken and sCode are from API call
                                    userDao.insert(new User(
                                            username.toString(),
                                            password.toString(),
                                            email.toString(),
                                            name.toString(),
                                            "TEST TOKEN",
                                            "TEST CODE"
                                    ));
                                } catch (android.database.sqlite.SQLiteConstraintException e) {
                                    // UI stuff and DB stuff can't be on same thread bc of thread locking
                                    // Need to create another thread to show toast
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(SignUpScreen.this, "The Username has been taken!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        }).start();
                }
            }
        });
    }
}
