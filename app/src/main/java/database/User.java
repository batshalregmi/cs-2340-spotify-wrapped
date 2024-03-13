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

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "spotify_token")
    public String sToken;

    @ColumnInfo(name = "spotify_code")
    public String sCode;

    public User(){}

    public User(String email, String password, String sToken, String sCode, String name) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.sToken = sToken;
        this.sCode = sCode;
        this.name = name;
    }

    @Override
    public String toString() {
        return (name + "\n" + email + "\n" + password + "\n" + sToken + "\n" + sCode);
    }
}
