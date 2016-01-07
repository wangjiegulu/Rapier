package com.wangjie.rapier.app.prefs;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;

import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: wangjie
 * Date: 14-3-29
 * Time: 上午12:18
 * To change this template use File | Settings | File Templates.
 */
public class PrefsHelper {
    public SharedPreferences prefs;
    public SharedPreferences.Editor editor;

    public PrefsHelper() {
    }


    public boolean getBoolean(String key, boolean defaultVal) {
        return this.prefs.getBoolean(key, defaultVal);
    }

    public boolean getBoolean(String key) {
        return this.prefs.getBoolean(key, false);
    }


    public String getString(String key, String defaultVal) {
        return this.prefs.getString(key, defaultVal);
    }

    public String getString(String key) {
        return this.prefs.getString(key, null);
    }

    public int getInt(String key, int defaultVal) {
        return this.prefs.getInt(key, defaultVal);
    }

    public int getInt(String key) {
        return this.prefs.getInt(key, 0);
    }


    public float getFloat(String key, float defaultVal) {
        return this.prefs.getFloat(key, defaultVal);
    }

    public float getFloat(String key) {
        return this.prefs.getFloat(key, 0f);
    }

    public long getLong(String key, long defaultVal) {
        return this.prefs.getLong(key, defaultVal);
    }

    public long getLong(String key) {
        return this.prefs.getLong(key, 0l);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key, Set<String> defaultVal) {
        return this.prefs.getStringSet(key, defaultVal);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> getStringSet(String key) {
        return this.prefs.getStringSet(key, null);
    }

    public Map<String, ?> getAll() {
        return this.prefs.getAll();
    }


    public PrefsHelper putString(String key, String value) {
        editor.putString(key, value);
//        editor.commit();
        return this;
    }

    public PrefsHelper putInt(String key, int value) {
        editor.putInt(key, value);
//        editor.commit();
        return this;
    }

    public PrefsHelper putFloat(String key, float value) {
        editor.putFloat(key, value);
//        editor.commit();
        return this;
    }

    public PrefsHelper putLong(String key, long value) {
        editor.putLong(key, value);
//        editor.commit();
        return this;
    }

    public PrefsHelper putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
//        editor.commit();
        return this;
    }

    public void commit() {
        editor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public PrefsHelper putStringSet(String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
        return this;
    }

    public PrefsHelper clear(){
        editor.clear();
        return this;
    }


}
