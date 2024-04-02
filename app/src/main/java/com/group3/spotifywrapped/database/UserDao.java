package com.group3.spotifywrapped.database;

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

    @Query("SELECT * FROM user WHERE username LIKE :username AND password LIKE :password LIMIT 1")
    List<User> findByLoginInfo(String username, String password);

    @Query("SELECT * FROM user WHERE username LIKE :username LIMIT 1")
    List<User> findByUsername(String username);

    @Query("SELECT * FROM user WHERE password LIKE :password LIMIT 1")
    List<User> findByPassword(String password);
}
