package org.meowcat.wearable.chain.heytap.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.heytap.wearable.support.widget.HeyBackTitleBar;

import org.meowcat.wearable.chain.BuildConfig;
import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.util.MyApplication;

public class AboutActivity extends AppCompatActivity
{
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_SWIPE_TO_DISMISS);
        setContentView(R.layout.activity_about);
        ctx = MyApplication.getContext();

        ((HeyBackTitleBar) findViewById(R.id.about_title)).getTitleTextView().setTextColor(getResources().getColor(R.color.gray));
        ((HeyBackTitleBar) findViewById(R.id.about_title)).a.setColorFilter(getResources().getColor(R.color.gray));

        findViewById(R.id.about_title).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        ((TextView) findViewById(R.id.about_ver)).setText(BuildConfig.VERSION_NAME);
    }
}
