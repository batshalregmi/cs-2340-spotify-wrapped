package com.group3.spotifywrapped.CoreAppViews;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.summary.SummaryActivity;
import com.group3.spotifywrapped.summary.SummarySelectorActivity;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    Dialog passwordDialog;
    Dialog emailDialog;
    Dialog deleteDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        emailDialog = new Dialog(this);
        passwordDialog = new Dialog(this);
        deleteDialog = new Dialog(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_wrapped:
                    Intent intent1 = new Intent(SettingsActivity.this, SummarySelectorActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.bottom_settings:
                    break;
            }


            return false;
        });

        ImageView profilePicture = findViewById(R.id.userProfilePicture);
        JSONObject userResponse = SpotifyApiHelper.callSpotifyApi("/me", LoginActivity.token, "GET");
        String imageUrl = null;
        Log.d("SettingsActivity", "User response: " + userResponse.toString());
        try {
            JSONArray images = userResponse.getJSONArray("images");
            Log.d("SettingsActivity", "User images: " + images.toString());
            if (images.length() > 0) {

                imageUrl = images.getJSONObject(images.length() - 1).getString("url");
                Log.d("SettingsActivity", "User image URL: " + imageUrl);

            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if (imageUrl != null) {
            Drawable image = null;
            try {
                image = SpotifyApiHelper.loadImageFromURL(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePicture.setImageDrawable(image);
        } else if (imageUrl == null) {
            Drawable image = null;
            try {
                image = SpotifyApiHelper.loadImageFromURL("https://www.pngitem.com/pimgs/m/146-1468479_my-profile-icon-blank-profile-picture-circle-hd.png");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            profilePicture.setImageDrawable(image);
        }
        Button changePasswordButton = findViewById(R.id.change_password);

        Button changeEmailButton = emailDialog.findViewById(R.id.changeEmailButton);
//        changeEmailButton.setOnClickListener(v -> {
//            FirebaseAuth user = FirebaseAuth.getInstance();
//            user.getCurrentUser().updateEmail(emailDialog.findViewById(R.id.newEmail).toString())
//                    .addOnCompleteListener(task -> {
//                        if (task.isSuccessful()) {
//                            Log.d("SettingsActivity", "User email address updated.");
//                            user.signOut();
//                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
//                            startActivity(intent);
//                        }
//                    });
//        });

        changePasswordButton.setOnClickListener(v -> {
            FirebaseAuth user = FirebaseAuth.getInstance();
            user.sendPasswordResetEmail(user.getCurrentUser().getEmail());
            user.signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Password reset link sent to email!", Toast.LENGTH_LONG).show();
        });
        //username.setText(LoginActivity.activeUser.username);


//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button changePasswordButton = findViewById(R.id.change_password);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button deleteAccountButton = findViewById(R.id.delete_account);
    }
    public void ShowPopupEmail(View v) {
        TextView txtclose;
        Button btnFollow;
        emailDialog.setContentView(R.layout.email_custompop);
        txtclose =(TextView) emailDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.dismiss();
            }
        });

        // Set up the changeEmailButton here, after the emailDialog has been inflated
        Button changeEmailButton = emailDialog.findViewById(R.id.changeEmailButton);
        EditText newEmail = emailDialog.findViewById(R.id.newEmail);
        changeEmailButton.setOnClickListener(x -> {
            FirebaseAuth user = FirebaseAuth.getInstance();
            Objects.requireNonNull(user.getCurrentUser()).updateEmail(newEmail.getText().toString())
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("SettingsActivity", "User email address updated.");
                            user.signOut();
                            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(this, "Email Updated, Please Login", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // This block will be executed if the task fails
                        Log.d("SettingsActivity", "Failed to update user email address.", e);
                    });
        });

        emailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        emailDialog.show();
    }
    public void ShowPopupPassword(View v) {
        TextView txtclose;
        Button btnFollow;
        passwordDialog.setContentView(R.layout.password_custompopup);
        txtclose = (TextView) passwordDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordDialog.dismiss();
            }
        });
        passwordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        passwordDialog.show();
    }
    public void ShowPopupDelete(View v) {
        TextView txtclose;
        Button btnFollow;
        deleteDialog.setContentView(R.layout.deleteaccount_custompopup);
        txtclose = (TextView) deleteDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDialog.dismiss();
            }
        });
        Button deleteButton = deleteDialog.findViewById(R.id.confirmAccountDeletion);
        deleteButton.setOnClickListener(q -> {
            FirebaseHelper.deleteUser(FirebaseAuth.getInstance().getCurrentUser());
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        deleteDialog.show();
    }

}
