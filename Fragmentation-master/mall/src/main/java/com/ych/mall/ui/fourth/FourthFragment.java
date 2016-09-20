package com.ych.mall.ui.fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseLazyMainFragment;
import com.ych.mall.ui.fourth.child.MeFragment;
import com.ych.mall.ui.third.child.ShopCarFragment;

/**
 * Created by ych on 2016/8/31.
 */
public class FourthFragment extends BaseLazyMainFragment{

    public static FourthFragment newInstance() {
        Bundle bundle = new Bundle();
        FourthFragment fragment = new FourthFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_fourth,null);
        return view;
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fourth_container, MeFragment.newInstance());
        }
    }
}
