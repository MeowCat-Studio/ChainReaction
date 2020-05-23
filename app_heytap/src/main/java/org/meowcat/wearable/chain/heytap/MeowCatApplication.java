package org.meowcat.wearable.chain.heytap;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.meowcat.wearable.chain.heytap.util.CrashHandler;

/**
 * Created by luern0313 on 2020/5/4.
 */
public class MeowCatApplication extends Application {
    public static String TAG = "ChainReaction";

    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(getApplicationContext());
        mContext = getApplicationContext();
    }
}
