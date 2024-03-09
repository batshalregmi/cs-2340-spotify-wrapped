package database;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Delete
    void delete(User user);
}
