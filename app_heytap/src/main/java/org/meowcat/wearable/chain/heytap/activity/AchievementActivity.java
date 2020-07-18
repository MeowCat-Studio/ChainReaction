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
import com.heytap.wearable.support.widget.HeyDialog;

import org.meowcat.wearable.chain.heytap.R;
import org.meowcat.wearable.chain.heytap.adapter.AchievementAdapter;
import org.meowcat.wearable.chain.heytap.model.AchievementModel;


public class AchievementActivity extends AppCompatActivity {
    Context ctx;
    LayoutInflater inflater;
    AchievementAdapter achievementAdapter;

    View layoutTitle;
    View layoutWipeButton;
    ListView uiListView;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.activity_achievement);
        ctx = this;
        inflater = getLayoutInflater();

        layoutTitle = inflater.inflate(R.layout.widget_achievement_title, null);
        layoutWipeButton = inflater.inflate(R.layout.widget_achievement_wipe, null);
        ((HeyBackTitleBar) layoutTitle.findViewById(R.id.widget_achievement_title)).getTitleTextView().setTextColor(getResources().getColor(R.color.gray));
        ((HeyBackTitleBar) layoutTitle.findViewById(R.id.widget_achievement_title)).a.setColorFilter(getResources().getColor(R.color.gray));
        uiListView = findViewById(R.id.achievement_listview);
        uiListView.addHeaderView(layoutTitle);
        uiListView.addFooterView(layoutWipeButton);

        achievementAdapter = new AchievementAdapter(inflater);
        uiListView.setAdapter(achievementAdapter);

        layoutTitle.findViewById(R.id.widget_achievement_title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        layoutWipeButton.findViewById(R.id.widget_achievement_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HeyDialog.HeyBuilder(ctx).setContentViewStyle(HeyDialog.STYLE_CONTENT)
                        .setMessage(getString(R.string.achievement_clear_message))
                        .setNegativeButton(getString(R.string.achievement_clear_cancel), null)
                        .setPositiveButton(getString(R.string.achievement_clear_ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AchievementModel.getInstance().clearAchievement();
                                achievementAdapter.notifyDataSetChanged();
                            }
                        }).create().show();
            }
        });
    }
}
