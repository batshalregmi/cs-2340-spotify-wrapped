package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @NonNull
    @PrimaryKey
    public String username;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "name")
    public String name;

    public String sToken;

    public String sCode;

    public User(){}

    public User(@NonNull String username, String password, String email, String name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return ("User");
    }
}
