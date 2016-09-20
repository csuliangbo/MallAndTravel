package com.ych.mall.ui.third;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseLazyMainFragment;

import com.ych.mall.ui.third.child.ShopCarFragment;


/**
 * Created by ych on 2016/8/31.
 */
public class ThridFragment extends BaseLazyMainFragment{

    public static ThridFragment newInstance() {
        Bundle bundle = new Bundle();
        ThridFragment fragment = new ThridFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frgment_thrid,null);
        return view;
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.third_container, ShopCarFragment.newInstance());
        }
    }
}