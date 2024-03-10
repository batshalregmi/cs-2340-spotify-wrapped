package database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @NonNull
    @PrimaryKey
    public String email;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "spotify_token")
    public String spotifyToken;

    @ColumnInfo(name = "spotify_code")
    public String spotifyCode;


}
