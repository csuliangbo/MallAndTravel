package com.ych.mall.ui.second;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseLazyMainFragment;
import com.ych.mall.ui.second.child.SortFragment;

/**
 * Created by ych on 2016/8/31.
 */
public class SecondFragment extends BaseLazyMainFragment{

    public static SecondFragment newInstance() {
        Bundle bundle = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_second,null);
        return view;
    }

    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.second_container, SortFragment.newInstance());
        }
    }
}