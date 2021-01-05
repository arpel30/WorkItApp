package com.example.workitapp;

import android.app.Application;

import com.example.workitapp.MySPV;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySPV.init(this);

    }
}
