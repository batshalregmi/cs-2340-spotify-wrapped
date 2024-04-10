package com.group3.spotifywrapped.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.group3.spotifywrapped.database.Artist;

import java.util.ArrayList;
import java.util.List;

public class SummaryViewModel extends ViewModel {
    private final MutableLiveData<List<Artist>> uiState = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<Artist>> getUiState() { return uiState; }
}
