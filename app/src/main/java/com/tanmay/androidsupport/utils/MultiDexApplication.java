package com.tanmay.androidsupport.utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by TaNMay on 4/23/2016.
 */
public class MultiDexApplication extends Application {
    public MultiDexApplication() {
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
