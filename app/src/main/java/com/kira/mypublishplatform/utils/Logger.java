package com.kira.mypublishplatform.utils;

import android.util.Log;
import com.kira.mypublishplatform.base.MyApplication;

/**
 *
 */
public class Logger {
    private static final String TAG = "Logger";

    /**
     * debug级别
     *
     * @param msg
     */
    public static void d(String msg) {
        if (MyApplication.Companion.isDebug()) {
            Log.d(TAG, msg);
        }
    }

    /**
     * info级别
     *
     * @param msg
     */
    public static void i(String msg) {
        if (MyApplication.Companion.isDebug()) {
            Log.i(TAG, msg);
        }
    }

    /**
     * warning级别
     *
     * @param msg
     */
    public static void w(String msg) {
        if (MyApplication.Companion.isDebug()) {
            Log.w(TAG, msg);
        }
    }

    /**
     * error级别
     *
     * @param msg
     */
    public static void e(String msg) {
        if (MyApplication.Companion.isDebug()) {
            Log.e(TAG, msg);
        }
    }
}
