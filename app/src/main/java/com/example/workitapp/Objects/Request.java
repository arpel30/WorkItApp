package com.example.workitapp.Objects;

import java.util.Date;

public class Request {
    String userId;
    Worker worker;
    Date date;

    public Request() {
    }

    public Request(String userId) {
        this.userId = userId;
        date = new Date();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
