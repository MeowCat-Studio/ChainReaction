package org.meowcat.wearable.chain.heytap.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 被 luern0313 创建于 2020/5/4.
 */
public class SharedPreferencesUtil
{
    public static String game = "game";
    public static String animSpeed = "anim_speed";
    public static String achievement = "achievement";

    private SharedPreferences sharedPreferences;
    private android.content.SharedPreferences.Editor editor;

    public SharedPreferencesUtil()
    {
        sharedPreferences = MyApplication.getContext().getSharedPreferences("default", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public boolean contains(String key)
    {
        return sharedPreferences.contains(key);
    }

    public String getString(String key, String def)
    {
        return sharedPreferences.getString(key, def);
    }

    public boolean putString(String key, String value)
    {
        return editor.putString(key, value).commit();
    }

    public int getInt(String key, int def)
    {
        return sharedPreferences.getInt(key, def);
    }

    public boolean putInt(String key, int value)
    {
        return editor.putInt(key, value).commit();
    }

    public boolean removeValue(String key)
    {
        return editor.remove(key).commit();
    }
}
