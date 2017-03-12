package net.yrom.screenrecorder.application;

import android.app.Application;
import android.content.Context;

/**
 * Created by raomengyang on 12/03/2017.
 */

public class MyApplication extends Application {

    static {
        System.loadLibrary("screenrecorderrtmp");
    }

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
