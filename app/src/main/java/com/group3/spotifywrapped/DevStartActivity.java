package com.group3.spotifywrapped;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.group3.spotifywrapped.database.Artist;
import com.group3.spotifywrapped.database.testing.LibraryDao;
import com.group3.spotifywrapped.database.testing.TestDatabase;
import com.group3.spotifywrapped.database.Track;

import java.util.List;
import java.util.Map;

public class DevStartActivity extends AppCompatActivity {
    private LibraryDao libraryDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dev_start);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_top_songs_summary), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TestDatabase db = TestDatabase.getInstance(this);
        libraryDao = db.libraryDao();

        //addTestData();

        Thread testQuery = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<Artist, List<Track>> artists = libraryDao.getAllArtistsWithTracks();
                Log.d("DevStartActivity", artists.toString());
            }
        });
        testQuery.start();
    }

    /*
    private void addTestData() {
        Thread addTestDataThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Artist artist1 = new Artist(1, "Artist2");
                Track track1 = new Track(2, 1, "Track3");
                Track track2 = new Track(3, 1, "Track4");

                libraryDao.insertArtist(artist1);
                libraryDao.insertTrack(track1);
                libraryDao.insertTrack(track2);
            }
        });
        addTestDataThread.start();
        try {
            addTestDataThread.join();
        } catch (Exception e) {
            Log.e("LoginActivity", e.toString());
        }
    }
     */
}