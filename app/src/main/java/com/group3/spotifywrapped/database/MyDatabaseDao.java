package com.group3.spotifywrapped.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDatabaseDao {
    //User methods
    @Insert
    void insertUser(User user);
    @Delete
    void deleteUser(User user);
    @Update
    void updateUser(User user);

    @Query("SELECT * FROM user WHERE id LIKE :id")
    List<User> findUsersById(long id);
    @Query("SELECT id FROM user")
    List<Long> getUserIdList();
    @Query("SELECT * FROM user WHERE username LIKE :username AND password LIKE :password LIMIT 1")
    List<User> findByLoginInfo(String username, String password);

    //SummaryEntry methods
    @Insert
    void insertSummaryEntry(SummaryEntry summaryEntry);
    @Delete
    void deleteSummaryEntry(SummaryEntry summaryEntry);
    @Update
    void updateSummaryEntry(SummaryEntry summaryEntry);

    @Query("SELECT id FROM summaryentry")
    List<Long> getSummaryEntryIdList();
    @Query("SELECT * FROM summaryentry WHERE id LIKE :id")
    List<SummaryEntry> findSummaryEntriesById(long id);

    //Artist methods
    @Insert
    void insertArtist(Artist artist);
    @Delete
    void deleteArtist(Artist artist);
    @Update
    void updateArtist(Artist artist);

    @Query("SELECT id FROM artist")
    List<Long> getArtistIdList();
    @Query("SELECT * FROM artist WHERE id LIKE :id")
    List<Artist> findArtistsById(long id);
    @Query("SELECT * FROM artist WHERE summary_entry_id = :id")
    List<Artist> getArtistsBySummaryEntry(long id);

    //Track methods
    @Insert
    void insertTrack(Track track);
    @Delete
    void deleteTrack(Track track);
    @Update
    void updateTrack(Track track);

    @Query("SELECT id FROM track")
    List<Long> getTrackIdSet();
    @Query("SELECT * FROM track WHERE id LIKE :id")
    List<Track> findTracksById(long id);
    @Query("SELECT * FROM track WHERE summary_entry_id = :id")
    List<Track> getTracksBySummaryEntry(long id);
}
