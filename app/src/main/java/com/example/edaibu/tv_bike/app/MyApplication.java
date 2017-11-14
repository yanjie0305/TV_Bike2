package com.example.edaibu.tv_bike.app;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.example.edaibu.tv_bike.util.SPUtil;


/**
 * Created by ${gyj} on 2017/9/19.
 */

public class MyApplication extends Application {
    public static MyApplication application;
    public static SPUtil spUtil;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        application = this;
        spUtil = SPUtil.getInstance(this);
    }

}
