package com.kira.mypublishplatform.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    private static final String SPH = "mysp";
    private SharedPreferences sp;
//    private SharedPreferences.Editor editor;

    public SharedPreferencesUtils(Context context) {
        sp = context.getSharedPreferences(SPH, Context.MODE_PRIVATE);
//        editor = sp.edit();
    }

    /**
     * 存入
     */
    public boolean putInt(String key, int value) {
//        editor.putInt(key, value);
        return sp.edit().putInt(key, value).commit();
    }

    /**
     * 取出
     */
    public int getInt(String key) {
        return sp.getInt(key, 0);
    }

    /**
     * 取出存入Float类型数据
     */
    public boolean putFloat(String key, float value) {
//        editor.putFloat(key, value);
        return sp.edit().putFloat(key, value).commit();
    }

    public float getFloat(String key) {
        return sp.getFloat(key, -1);
    }

    /**
     * 取出存入Double类型数据
     */
    static SharedPreferences.Editor putDouble(final SharedPreferences.Editor edit, final String key, final double value) {
        return edit.putLong(key, Double.doubleToRawLongBits(value));
    }

    public boolean putDouble(String key, double value) {
//        editor.putLong(key, Double.doubleToRawLongBits(value));
        return sp.edit().putLong(key, Double.doubleToRawLongBits(value)).commit();
    }

    static double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
        if (!prefs.contains(key))
            return defaultValue;
        return Double.longBitsToDouble(prefs.getLong(key, 0));
    }

    public double getDouble(String key) {return Double.longBitsToDouble(sp.getLong(key, 0));}

    /**
     * 存入字符串
     */
    public boolean putStr(String key, String value) {
//        editor.putString(key, value);
        return sp.edit().putString(key, value).commit();
    }

    /**
     * 取出字符串
     */
    public String getStr(String key) {
        return sp.getString(key, "");
    }


    /**
     * 存入Boolean类型数据
     */
    public boolean putBoolean(String key, boolean value) {
//        editor.putBoolean(key, value);
        return sp.edit().putBoolean(key, value).commit();
    }

    /**
     * 取出Boolean类型数据
     */
    public boolean getBoolean(String key) {
        return sp.getBoolean(key, false);
    }

    /**
     * 存入Long类型数据
     */
    public boolean putLong(String key, long value) {
//        editor.putLong(key, value);
        return sp.edit().putLong(key, value).commit();
    }

    /**
     * 取出Long类型数据
     */
    public long getLong(String key) {
        return sp.getLong(key, 0);
    }

    /**
     * 清除
     */
    public boolean clear() {
//        editor.clear();
        return sp.edit().clear().commit();
    }

    /**
     * 关闭
     */
    public void close() {
        sp = null;
    }

}
