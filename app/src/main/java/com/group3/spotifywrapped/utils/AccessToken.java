package com.group3.spotifywrapped.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class AccessToken {
    private AtomicReference<String> mToken;
    private LocalDateTime lastTokenGeneratedDate;
    private Thread updateThread;

    public AccessToken(String initialToken) {
        this.mToken = new AtomicReference<>(initialToken);
        this.lastTokenGeneratedDate = LocalDateTime.now();
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (LocalDateTime.now().isBefore(lastTokenGeneratedDate.plusMinutes(50))) {
                        renewToken();
                    }
                }
            }
        });
        updateThread.start();
    }

    public String getToken() throws IllegalAccessError {
        return mToken.get();
    }

    private void renewToken() {

    }
}
