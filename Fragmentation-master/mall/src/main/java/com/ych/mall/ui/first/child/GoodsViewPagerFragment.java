package com.ych.mall.ui.first.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ych.mall.R;
import com.ych.mall.adapter.GoodsPagerAdapter;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ych on 2016/8/31.
 */
@EFragment(R.layout.fragment_goods_viewpager)
public class GoodsViewPagerFragment extends BaseFragment {

    @ViewById(R.id.tab)
    TabLayout mTab;
    @ViewById(R.id.viewPager)
    ViewPager mViewPager;
    String id;

    public static GoodsViewPagerFragment newInstance(int type, String id) {
        Bundle bundle = new Bundle();
        bundle.putInt(KV.TYPE, type);
        bundle.putString(KV.ID, id);
        GoodsViewPagerFragment fragment = new GoodsViewPagerFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    public void initView() {
        id = getArguments().getString(KV.ID);
        mTab.addTab(mTab.newTab());
        mTab.addTab(mTab.newTab());
        //mTab.addTab(mTab.newTab());
        mViewPager.setAdapter(new GoodsPagerAdapter(getChildFragmentManager(),getArguments().getInt(KV.TYPE),id));
        mTab.setupWithViewPager(mViewPager);
        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("商品"))
                    mViewPager.setCurrentItem(0, false);
                if (tab.getText().toString().equals("详情"))
                    mViewPager.setCurrentItem(1, false);
                if (tab.getText().toString().equals("评价"))
                    mViewPager.setCurrentItem(2, false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

}
