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

    @Query("SELECT name FROM user WHERE email LIKE :email")
    String getName(String email);

    @Query("SELECT password FROM user WHERE email LIKE :email LIMIT 1")
    String getPassword(String email);

    @Query("SELECT * FROM user WHERE email LIKE :email LIMIT 1")
    List<User> findByEmail(String email);
}
