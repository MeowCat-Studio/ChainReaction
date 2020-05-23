package org.meowcat.wearable.chain.heytap.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by luern0313 on 2020/5/5.
 */
public class MainViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragments;

    public MainViewPagerAdapter(@NonNull FragmentManager fragmentManager, List<Fragment> fragments) {
        super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragments = fragments;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
