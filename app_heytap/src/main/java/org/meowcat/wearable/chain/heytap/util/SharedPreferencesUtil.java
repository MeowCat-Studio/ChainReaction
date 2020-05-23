package org.meowcat.wearable.chain.heytap.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.meowcat.wearable.chain.heytap.MeowCatApplication;

/**
 * Created by luern0313 on 2020/5/4.
 */
public class SharedPreferencesUtil {
    public static String game = "game";
    public static String animSpeed = "anim_speed";
    public static String achievement = "achievement";

    private SharedPreferences sharedPreferences;
    private android.content.SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SharedPreferencesUtil() {
        sharedPreferences = MeowCatApplication.getContext().getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public String getString(String key, String def) {
        return sharedPreferences.getString(key, def);
    }

    public void putString(String key, String value) {
        editor.putString(key, value).commit();
    }

    public int getInt(String key, int def) {
        return sharedPreferences.getInt(key, def);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value).commit();
    }

    public void removeValue(String key) {
        editor.remove(key).commit();
    }
}
