package com.ych.mall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ych.mall.ui.first.child.childpager.CommentFragment;
import com.ych.mall.ui.first.child.childpager.DetailFragment;
import com.ych.mall.ui.first.child.childpager.GoodsFragment;

/**
 * Created by ych on 2016/9/5.
 */
public class GoodsPagerAdapter extends FragmentPagerAdapter {
    //private String[] mTab = new String[]{"商品", "详情", "评价"};
    private String[] mTab = new String[]{"商品", "详情"};
    private String mId;

    public GoodsPagerAdapter(FragmentManager fm, int type, String id) {
        super(fm);
        this.type = type;
        mId = id;
    }

    int type;


    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return GoodsFragment.newInstance(type, mId);
        else if (position==1)
            return DetailFragment.newInstance(type,mId);
        else
            return CommentFragment.newInstance(type,mId);
//        else if (position == 1)
//            return DetailFragment.newInstance();
//       else
//           return CommentFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTab[position];
    }
}
