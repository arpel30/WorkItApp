package com.example.workitapp.More;

import android.app.Application;

import com.example.workitapp.Objects.MySPV;

public class App  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MySPV.init(this);

    }
}
