package com.brandanquinn.casino.casino;

import android.app.Application;
import android.content.Context;

public class AppContextProvider extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
    }

    public static Context getAppContext() {
        return appContext;
    }

    private static Context appContext;
}
