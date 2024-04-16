package com.group3.spotifywrapped.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LiveDataList<E> extends ArrayList<E> {
    private List<ElementObserver> observers = new ArrayList<>();

    public LiveDataList() {}

    private void notifyObservers() {
        for (ElementObserver it : observers) {
            it.onChange();
        }
    }

    public void addObserver(ElementObserver observer) {
        observers.add(observer);
    }
    public void clearObservers() { observers.clear(); }

    @Override
    public boolean add(E e) {
        boolean result = super.add(e);
        notifyObservers();
        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result = super.addAll(c);
        notifyObservers();
        return result;
    }

    @Override
    public void clear() {
        super.clear();
        notifyObservers();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
