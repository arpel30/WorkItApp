package com.example.workitapp.Objects;

import android.os.Build;

import java.time.LocalDate;
import java.util.Date;

public class Request {
    String userId;
    Worker worker;
    LocalDate date;

    public Request() {
    }

    public Request(String userId) {
        this.userId = userId;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
        }
    }

    public Request(Worker worker) {
        this.worker = worker;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDate.now();
        }
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
