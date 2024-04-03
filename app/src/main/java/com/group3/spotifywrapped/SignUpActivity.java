package com.group3.spotifywrapped;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.group3.spotifywrapped.database.AppDatabase;
import com.group3.spotifywrapped.database.User;
import com.group3.spotifywrapped.database.UserDao;


public class SignUpActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText confirmPassword;
    EditText email;
    EditText name;
    Button createAccountButton;

    public AppDatabase db;
    public static UserDao userDao;
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
                        //dbUsername = userDao.getUsername(username.getText().toString().toLowerCase());
                    }
                });
                thread.start();

                // TODO: This is kind of buggy, will fix tonight
                // Only works with emails with the password "password"

                if (username.getText().toString().equals(dbUsername)) {
                    Toast.makeText(SignUpActivity.this, "The Username has been taken!", Toast.LENGTH_SHORT).show();
                }
                if(!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "Your passwords do not match!", Toast.LENGTH_SHORT).show();
                } else {
                    addNewUser(username.getText().toString(), password.getText().toString(), name.getText().toString(), email.getText().toString());
                    Toast.makeText(SignUpActivity.this, "New Account Created!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addNewUser(String user, String pass, String fName, String sEmail) {
        User trentUser = new User(
                user,
                pass,
                sEmail,
                fName
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