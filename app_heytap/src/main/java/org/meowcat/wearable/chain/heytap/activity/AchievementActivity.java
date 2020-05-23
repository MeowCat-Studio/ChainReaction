package org.meowcat.wearable.chain.heytap.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.heytap.wearable.support.widget.HeyBackTitleBar;

import org.meowcat.wearable.chain.heytap.MeowCatApplication;
import org.meowcat.wearable.chain.heytap.R;
import org.meowcat.wearable.chain.heytap.adapter.AchievementAdapter;


public class AchievementActivity extends AppCompatActivity {
    Context ctx;
    LayoutInflater inflater;
    AchievementAdapter achievementAdapter;

    View layoutTitle;
    ListView uiListView;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.activity_achievement);
        ctx = MeowCatApplication.getContext();
        inflater = getLayoutInflater();

        layoutTitle = inflater.inflate(R.layout.widget_achievement_title, null);
        ((HeyBackTitleBar) layoutTitle.findViewById(R.id.widget_achievement_title)).getTitleTextView().setTextColor(getResources().getColor(R.color.gray));
        ((HeyBackTitleBar) layoutTitle.findViewById(R.id.widget_achievement_title)).a.setColorFilter(getResources().getColor(R.color.gray));
        uiListView = findViewById(R.id.achievement_listview);
        uiListView.addHeaderView(layoutTitle);

        achievementAdapter = new AchievementAdapter(inflater);
        uiListView.setAdapter(achievementAdapter);

        layoutTitle.findViewById(R.id.widget_achievement_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
