package com.wangjie.rapier.app;

import android.app.Application;

/**
 * Author: wangjie
 * Email: tiantian.china.2@gmail.com
 * Date: 1/5/16.
 */
public class RapierApplication extends Application{
    public static RapierApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }



}
