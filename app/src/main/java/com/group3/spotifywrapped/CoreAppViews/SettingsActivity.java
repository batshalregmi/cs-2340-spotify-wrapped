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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.group3.spotifywrapped.R;
import com.group3.spotifywrapped.database.FirebaseHelper;
import com.group3.spotifywrapped.utils.SpotifyApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {
    Dialog passwordDialog;
    Dialog emailDialog;

    String changedPassword;
    String changedEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        emailDialog = new Dialog(this);
        passwordDialog = new Dialog(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_wrapped:
                    Intent intent1 = new Intent(SettingsActivity.this, MainPageActivity.class);
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
        TextView username = findViewById(R.id.usernameUnderPicture);
        //username.setText(LoginActivity.activeUser.username);


        Button deleteButton = findViewById(R.id.delete_account);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button changeEmailButton = findViewById(R.id.change_email);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button changePasswordButton = findViewById(R.id.change_password);
        deleteButton.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseHelper.deleteUser(FirebaseAuth.getInstance().getCurrentUser());
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



    }
    public void ShowPopupEmail(View v) {
        TextView txtclose;
        Button btnFollow;
        emailDialog.setContentView(R.layout.email_custompop);
        txtclose = (TextView) emailDialog.findViewById(R.id.txtclose);
        txtclose.setText("X");

        Button changeEmailSubmitButton = emailDialog.findViewById(R.id.changeEmailButton);
        EditText newEmailEditText = emailDialog.findViewById(R.id.newEmail); // Get the EditText for new email

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailDialog.dismiss();
            }
        });

        emailDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        emailDialog.show();

        changeEmailSubmitButton.setOnClickListener(x -> {
            changedEmail = newEmailEditText.getText().toString(); // Get the new email from EditText
            TextView emailNowPassword = emailDialog.findViewById(R.id.confirm_email);
            if (changedEmail != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication
                AuthCredential credential = EmailAuthProvider
                        .getCredential("regmibat@gmail.com", "123456"); // Current Login Credentials \\
                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("SETTINGSPAGEACTIVITY", "User re-authenticated.");
                                //Now change your email address \\
                                //----------------Code for Changing Email Address----------\\
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.updateEmail(changedEmail)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("SETTINGSPAGEACTIVITY", "User email address updated.");
                                                }
                                            }
                                        });
                                //----------------------------------------------------------\\
                            }
                        });
            }
            emailDialog.dismiss();
        });
    }

    public void ShowPopupPassword(View v) {
        TextView txtclose;
        Button btnFollow;
        passwordDialog.setContentView(R.layout.password_custompopup);
        txtclose =(TextView) passwordDialog.findViewById(R.id.txtclose);
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

}
