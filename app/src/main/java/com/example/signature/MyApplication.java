package com.example.signature;

import android.app.Application;

public class MyApplication extends Application {

    private static MyApplication context;

    // get context
    public static MyApplication getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        context = this;
        super.onCreate();
    }
}
