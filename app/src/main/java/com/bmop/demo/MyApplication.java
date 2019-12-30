package com.bmop.demo;

import android.app.Application;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.v3.Bmob;

public class MyApplication extends Application {
    private ImageLoader loader;

    @Override
    public void onCreate() {
        super.onCreate();
        Bmob.initialize(this, "84a5215419e9b59cbc5ec6cc39d11316");

        if (loader == null) {
            loader = ImageLoader.getInstance();
        }
        if (!loader.isInited()) {
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this);
            builder.writeDebugLogs();
            loader.init(builder.build());
        }
    }
}
