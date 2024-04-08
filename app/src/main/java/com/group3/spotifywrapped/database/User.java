package com.group3.spotifywrapped.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

import com.group3.spotifywrapped.CoreAppViews.LoginActivity;

public class User {
    public String Uid;
    public String username;
    public String name;

    @Ignore
    public User(){}
    public User(String username, String name) {
        this.username = username;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("User::%s", name);
    }
}
