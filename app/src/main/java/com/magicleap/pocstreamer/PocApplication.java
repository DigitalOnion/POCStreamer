package com.magicleap.pocstreamer;

import android.app.Application;
import android.content.Context;

public class PocApplication extends Application {
    private static PocApplication instance;

    public static PocApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public static String appName() {
        return instance.getClass().getName();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
