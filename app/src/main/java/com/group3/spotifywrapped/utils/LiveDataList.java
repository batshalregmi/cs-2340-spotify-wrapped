package com.group3.spotifywrapped.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class LiveDataList<E> extends ArrayList<E> {
    private List<DataListener> listeners = new ArrayList<>();

    public LiveDataList() {}

    private void updateListeners() {
        for (DataListener it : listeners) {
            it.run();
        }
    }

    public void addListener(DataListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        updateListeners();
        return result;
    }

    @Override
    public void clear() {
        super.clear();
        updateListeners();
    }
}
