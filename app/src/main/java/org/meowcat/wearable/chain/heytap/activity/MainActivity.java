package org.meowcat.wearable.chain.heytap.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;

import org.meowcat.wearable.chain.R;
import org.meowcat.wearable.chain.heytap.fragment.MenuFragment;
import org.meowcat.wearable.chain.heytap.fragment.TitleFragment;
import org.meowcat.wearable.chain.heytap.adapter.MainViewPagerAdapter;
import org.meowcat.wearable.chain.heytap.fragment.ChessFragment;
import org.meowcat.wearable.chain.heytap.fragment.TutorialFragment;
import org.meowcat.wearable.chain.heytap.model.ChessModel;
import org.meowcat.wearable.chain.heytap.util.MyApplication;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TitleFragment.TitleFragmentListener, MenuFragment.MenuFragmentListener, TutorialFragment.TutorialFragmentListener
{
    Context ctx;

    ViewPager viewPager;
    List<Fragment> fragmentList;
    MainViewPagerAdapter mainViewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ctx = MyApplication.getContext();

        viewPager = findViewById(R.id.main_viewpager);

        fragmentList = new ArrayList<>();
        fragmentList.add(new TitleFragment());
        fragmentList.add(MenuFragment.newInstance(null));
        mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(mainViewPagerAdapter);
    }

    @Override
    public void onTitleFragmentStart(ChessModel chessModel)
    {
        fragmentList.remove(0);
        fragmentList.add(0, ChessFragment.newInstance(chessModel));
        mainViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTitleFragmentTutorial()
    {
        fragmentList.remove(0);
        fragmentList.add(0, TutorialFragment.newInstance());
        mainViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTitleFragmentMenu()
    {
        viewPager.setCurrentItem(1, true);
    }

    @Override
    public void onMenuFragmentString(ChessModel chessModel)
    {
        fragmentList.remove(0);
        fragmentList.add(0, ChessFragment.newInstance(chessModel));
        mainViewPagerAdapter.notifyDataSetChanged();
        viewPager.setCurrentItem(0, true);
    }

    @Override
    public void onTutorialFragmentNew()
    {

    }
}
