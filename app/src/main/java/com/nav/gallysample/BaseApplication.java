package com.nav.gallysample;

import android.app.Application;

import io.multimoon.colorful.Defaults;
import io.multimoon.colorful.ThemeColor;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Defaults defaults = new Defaults(ThemeColor.CYAN,
                ThemeColor.GREEN,
                false,
                false, 0);

    }
}
