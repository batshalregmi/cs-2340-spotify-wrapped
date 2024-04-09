package com.group3.spotifywrapped.utils;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;

public class DebugTimer {
    private class Split {
        private String name;
        private long timeLength;

        public Split(String name, long timeLength) {
            this.name = name;
            this.timeLength = timeLength;
        }

        public long getMilli() {
            return timeLength / 1000000;
        }
    }

    private Queue<Split> splits = new LinkedList<>();
    private long timerStart = 0;
    private String name;

    public DebugTimer(String name) {
        this.name = name;
        this.timerStart = System.nanoTime();
    }

    public void addSplit(String splitName) {
        splits.add(new Split(splitName, System.nanoTime() - timerStart));
        timerStart = System.nanoTime();
    }
    public void dumpToLog() {
        Log.d("Timer", String.format("%s: begin", name));
        while (splits.peek() != null) {
            Split temp = splits.poll();
            Log.d("Timer", String.format("%s: %d ms, %s", name, temp.getMilli(), temp.name));
        }
        Log.d("Timer", String.format("%s: end", name));
    }
}
