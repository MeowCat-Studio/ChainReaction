package org.meowcat.wearable.chain.heytap.model;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.meowcat.wearable.chain.heytap.R;
import org.meowcat.wearable.chain.heytap.util.SharedPreferencesUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;

import lombok.Data;

/**
 * Created by luern0313 on 2020/5/12.
 */
@Data
public class AchievementModel {
    private static AchievementModel instance = new AchievementModel();

    private SharedPreferencesUtil sharedPreferencesUtil;
    private LinkedHashMap<String, Integer> achievementMap;

    public static final String achievementBase1 = "base_1";
    public static final String achievementBase2 = "base_2";
    public static final String achievementBase3 = "base_3";
    public static final String achievementAi1 = "ai_1";
    public static final String achievementAi2 = "ai_2";
    public static final String achievementAi3 = "ai_3";
    public static final String achievementAi4 = "ai_4";

    private HashMap<String, Integer> achievementStringMap = new HashMap<String, Integer>(){{
        put(achievementBase1, R.string.achievement_base_1);
        put(achievementBase2, R.string.achievement_base_2);
        put(achievementBase3, R.string.achievement_base_3);
        put(achievementAi1, R.string.achievement_ai_1);
        put(achievementAi2, R.string.achievement_ai_2);
        put(achievementAi3, R.string.achievement_ai_3);
        put(achievementAi4, R.string.achievement_ai_4);
    }};

    private AchievementModel() {
        sharedPreferencesUtil = new SharedPreferencesUtil();
        achievementMap = new LinkedHashMap<String, Integer>() {{
            put(achievementBase1, 0);
            put(achievementBase2, 0);
            put(achievementBase3, 0);
            put(achievementAi1, 0);
            put(achievementAi2, 0);
            put(achievementAi3, 0);
            put(achievementAi4, 0);
        }};
        try {
            JSONObject ach = new JSONObject(sharedPreferencesUtil.getString(SharedPreferencesUtil.achievement, "{}"));
            String[] keys = achievementMap.keySet().toArray(new String[]{});
            for (String key : keys)
                achievementMap.put(key, ach.optInt(key, 0));
        } catch (JSONException | RuntimeException e) {
            e.printStackTrace();
        }
    }

    public static AchievementModel getInstance() {
        return instance;
    }

    public void recordAchievement(String key) {
        recordAchievement(key, 1);
    }

    @SuppressWarnings("ConstantConditions")
    public void recordAchievement(String key, int value) {
        if (achievementMap.containsKey(key))
            achievementMap.put(key, achievementMap.get(key) + value);
    }

    public void clearAchievement() {
        sharedPreferencesUtil.removeValue(SharedPreferencesUtil.achievement);
        String[] keys = achievementMap.keySet().toArray(new String[]{});
        for (String key : keys)
            achievementMap.put(key, 0);
    }

    @NonNull
    @Override
    public String toString() {
        JSONObject ach = new JSONObject();
        try {
            String[] keys = achievementMap.keySet().toArray(new String[]{});
            for (String key : keys)
                ach.put(key, achievementMap.get(key));
        } catch (JSONException | RuntimeException e) {
            e.printStackTrace();
        }
        return ach.toString();
    }
}
