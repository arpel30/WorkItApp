package com.example.workitapp.Objects;

import android.os.Build;

import java.time.LocalDate;
import java.util.Date;

public class Request {
    String uid;
    String date;

    public Request() {
    }

    public Request(String uid) {
        this.uid = uid;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now().toString();
        }
    }

    @Override
    public String toString() {
        return "Request{" +
                "uid='" + uid + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
