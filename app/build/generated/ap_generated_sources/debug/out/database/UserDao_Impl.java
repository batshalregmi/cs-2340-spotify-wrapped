package database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<User> __insertionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __deletionAdapterOfUser;

  private final EntityDeletionOrUpdateAdapter<User> __updateAdapterOfUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUser = new EntityInsertionAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `User` (`username`,`password`,`email`,`name`,`spotify_token`,`spotify_code`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        if (entity.username == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.username);
        }
        if (entity.password == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.password);
        }
        if (entity.email == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.email);
        }
        if (entity.name == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.name);
        }
        if (entity.sToken == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.sToken);
        }
        if (entity.sCode == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.sCode);
        }
      }
    };
    this.__deletionAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `User` WHERE `username` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        if (entity.username == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.username);
        }
      }
    };
    this.__updateAdapterOfUser = new EntityDeletionOrUpdateAdapter<User>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `User` SET `username` = ?,`password` = ?,`email` = ?,`name` = ?,`spotify_token` = ?,`spotify_code` = ? WHERE `username` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement, final User entity) {
        if (entity.username == null) {
          statement.bindNull(1);
        } else {
          statement.bindString(1, entity.username);
        }
        if (entity.password == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.password);
        }
        if (entity.email == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.email);
        }
        if (entity.name == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.name);
        }
        if (entity.sToken == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.sToken);
        }
        if (entity.sCode == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.sCode);
        }
        if (entity.username == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.username);
        }
      }
    };
  }

  @Override
  public void insert(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void insertAll(final List<User> users) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUser.insert(users);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final User user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<User> getAll() {
    final String _sql = "SELECT * FROM user";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSToken = CursorUtil.getColumnIndexOrThrow(_cursor, "spotify_token");
      final int _cursorIndexOfSCode = CursorUtil.getColumnIndexOrThrow(_cursor, "spotify_code");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _item.username = null;
        } else {
          _item.username = _cursor.getString(_cursorIndexOfUsername);
        }
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _item.password = null;
        } else {
          _item.password = _cursor.getString(_cursorIndexOfPassword);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _item.email = null;
        } else {
          _item.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfSToken)) {
          _item.sToken = null;
        } else {
          _item.sToken = _cursor.getString(_cursorIndexOfSToken);
        }
        if (_cursor.isNull(_cursorIndexOfSCode)) {
          _item.sCode = null;
        } else {
          _item.sCode = _cursor.getString(_cursorIndexOfSCode);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String getName(final String username) {
    final String _sql = "SELECT name FROM user WHERE username LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if (_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String getPassword(final String username) {
    final String _sql = "SELECT password FROM user WHERE username LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if (_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public String getUsername(final String username) {
    final String _sql = "SELECT username FROM user WHERE username LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if (_cursor.moveToFirst()) {
        if (_cursor.isNull(0)) {
          _result = null;
        } else {
          _result = _cursor.getString(0);
        }
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<User> findByEmail(final String email) {
    final String _sql = "SELECT * FROM user WHERE email LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (email == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, email);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSToken = CursorUtil.getColumnIndexOrThrow(_cursor, "spotify_token");
      final int _cursorIndexOfSCode = CursorUtil.getColumnIndexOrThrow(_cursor, "spotify_code");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _item.username = null;
        } else {
          _item.username = _cursor.getString(_cursorIndexOfUsername);
        }
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _item.password = null;
        } else {
          _item.password = _cursor.getString(_cursorIndexOfPassword);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _item.email = null;
        } else {
          _item.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfSToken)) {
          _item.sToken = null;
        } else {
          _item.sToken = _cursor.getString(_cursorIndexOfSToken);
        }
        if (_cursor.isNull(_cursorIndexOfSCode)) {
          _item.sCode = null;
        } else {
          _item.sCode = _cursor.getString(_cursorIndexOfSCode);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<User> findByUsername(final String username) {
    final String _sql = "SELECT * FROM user WHERE username LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (username == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, username);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfSToken = CursorUtil.getColumnIndexOrThrow(_cursor, "spotify_token");
      final int _cursorIndexOfSCode = CursorUtil.getColumnIndexOrThrow(_cursor, "spotify_code");
      final List<User> _result = new ArrayList<User>(_cursor.getCount());
      while (_cursor.moveToNext()) {
        final User _item;
        _item = new User();
        if (_cursor.isNull(_cursorIndexOfUsername)) {
          _item.username = null;
        } else {
          _item.username = _cursor.getString(_cursorIndexOfUsername);
        }
        if (_cursor.isNull(_cursorIndexOfPassword)) {
          _item.password = null;
        } else {
          _item.password = _cursor.getString(_cursorIndexOfPassword);
        }
        if (_cursor.isNull(_cursorIndexOfEmail)) {
          _item.email = null;
        } else {
          _item.email = _cursor.getString(_cursorIndexOfEmail);
        }
        if (_cursor.isNull(_cursorIndexOfName)) {
          _item.name = null;
        } else {
          _item.name = _cursor.getString(_cursorIndexOfName);
        }
        if (_cursor.isNull(_cursorIndexOfSToken)) {
          _item.sToken = null;
        } else {
          _item.sToken = _cursor.getString(_cursorIndexOfSToken);
        }
        if (_cursor.isNull(_cursorIndexOfSCode)) {
          _item.sCode = null;
        } else {
          _item.sCode = _cursor.getString(_cursorIndexOfSCode);
        }
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
