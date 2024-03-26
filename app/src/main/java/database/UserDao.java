package database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);

    @Insert
    void insertAll(List<User> users);

    @Update
    void update(User user);

    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT name FROM user WHERE username LIKE :username LIMIT 1")
    String getName(String username);

    @Query("SELECT password FROM user WHERE username LIKE :username LIMIT 1")
    String getPassword(String username);

    @Query("SELECT username FROM user WHERE username LIKE :username LIMIT 1")
    String getUsername(String username);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    List<User> findByEmail(String email);

    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    List<User> findByUsername(String username);

    @Query("SELECT spotify_token FROM user WHERE username LIKE :username LIMIT 1")
    String getToken(String username);
}
