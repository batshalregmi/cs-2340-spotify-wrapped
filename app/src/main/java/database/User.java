package database;

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

    @ColumnInfo(name = "spotify_token")
    public String sToken;

    @ColumnInfo(name = "spotify_code")
    public String sCode;

    public User(){}

    public User(@NonNull String username, String password, String email, String name, String sToken, String sCode) {
        this.username = username.toLowerCase();
        this.password = password;
        this.email = email;
        this.name = name;
        this.sToken = sToken;
        this.sCode = sCode;
    }

    @Override
    public String toString() {
        return (name + "\n" + email + "\n" + password + "\n" + sToken + "\n" + sCode);
    }
}
