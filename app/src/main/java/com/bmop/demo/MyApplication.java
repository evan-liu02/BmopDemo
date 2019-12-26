package com.bmop.demo;

import android.app.Application;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "84a5215419e9b59cbc5ec6cc39d11316");
    }
}
