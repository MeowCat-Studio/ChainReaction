package org.meowcat.wearable.chain.heytap.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.meowcat.wearable.chain.heytap.util.SharedPreferencesUtil;

import java.util.LinkedHashMap;

import androidx.annotation.NonNull;
import lombok.Data;

/**
 * 被 luern0313 创建于 2020/5/12.
 */
@Data
public class AchievementModel
{
    private static AchievementModel instance = new AchievementModel();

    private SharedPreferencesUtil sharedPreferencesUtil;
    private LinkedHashMap<String, Integer> achievementMap;

    private AchievementModel()
    {
        sharedPreferencesUtil = new SharedPreferencesUtil();
        achievementMap = new LinkedHashMap<String, Integer>()
        {{
            put("游戏数", 0);
            put("总回合数", 0);
            put("旋转过箭头", 0);
            put("战胜新手AI", 0);
            put("战胜入门AI", 0);
            put("战胜熟练AI", 0);
            put("战胜大师AI", 0);
        }};
        try
        {
            JSONObject ach = new JSONObject(sharedPreferencesUtil.getString(SharedPreferencesUtil.achievement, "{}"));
            String[] keys = achievementMap.keySet().toArray(new String[]{});
            for(String key : keys)
                achievementMap.put(key, ach.optInt(key, 0));
        }
        catch (JSONException | RuntimeException e)
        {
            e.printStackTrace();
        }
    }

    public static AchievementModel getInstance()
    {
        return instance;
    }

    public void recordAchievement(String key)
    {
        recordAchievement(key, 1);
    }

    public void recordAchievement(String key, int value)
    {
        if(achievementMap.containsKey(key))
            achievementMap.put(key, achievementMap.get(key) + value);
    }

    @NonNull
    @Override
    public String toString()
    {
        JSONObject ach = new JSONObject();
        try
        {
            String[] keys = (String[]) achievementMap.keySet().toArray();
            for (String key : keys)
                ach.put(key, achievementMap.get(key));
        }
        catch(JSONException | RuntimeException e)
        {
            e.printStackTrace();
        }
        return ach.toString();
    }
}
