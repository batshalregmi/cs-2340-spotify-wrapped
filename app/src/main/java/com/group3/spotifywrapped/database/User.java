package com.group3.spotifywrapped.database;

import java.util.ArrayList;
import java.util.List;

public class User {
    //fields which get added directly to database
    public String userUid;
    public String username;
    public String name;

    public User(){}

    @Override
    public String toString() {
        return String.format("User::%s", name);
    }
}
