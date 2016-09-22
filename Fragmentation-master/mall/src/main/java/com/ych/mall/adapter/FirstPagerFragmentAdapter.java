package com.ych.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ych.mall.ui.first.child.childpager.HomeMallFragment;
import com.ych.mall.ui.first.child.childpager.HomeTravelFragment;


/**
 * Created by YoKeyword on 16/6/5.
 */
public class FirstPagerFragmentAdapter extends FragmentPagerAdapter {


    public FirstPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return HomeMallFragment.newInstance();
        } else {
            return HomeTravelFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
