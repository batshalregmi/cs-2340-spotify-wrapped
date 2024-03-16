package com.group3.spotifywrapped.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class AccessToken {
    private String mToken;
    private LocalDateTime lastTokenGeneratedDate;

    public AccessToken(String src) {
        String[] data = src.split(",");
        this.mToken = data[0];
        this.lastTokenGeneratedDate = LocalDateTime.parse(data[1]);
    }

    public String getToken() throws IllegalAccessError {
        Date currentDateTemp = Calendar.getInstance().getTime();
        LocalDateTime currentDate = currentDateTemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        currentDate.minusHours(1);
        if (currentDate.isAfter(lastTokenGeneratedDate)) {
            throw new IllegalAccessError("Token is old");
        }
        return mToken;
    }

    public void setToken(String mToken) throws IllegalAccessError {
        Date currentDateTemp = Calendar.getInstance().getTime();
        LocalDateTime currentDate = currentDateTemp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        currentDate.minusHours(1);
        if (currentDate.isBefore(lastTokenGeneratedDate)) {
            throw new IllegalAccessError("Token has not expired yet");
        }
        this.mToken = mToken;
        this.lastTokenGeneratedDate = currentDate;
    }
}
