package com.chengfu.android.fuplayer.demo;

import android.app.Application;

public class APP extends Application {

    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }
}
