package com.ych.mall.ui.first;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseLazyMainFragment;
import com.ych.mall.ui.first.child.ViewPagerFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by ych on 2016/8/31.
 */

public class FirstFragment extends BaseLazyMainFragment {

    private int FIRST = 0;
    private int SECOND = 1;
    private int current = 0;
    private SupportFragment[] fragments = new SupportFragment[2];

    public static FirstFragment newInstance() {
        Bundle bundle = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, null);
        initView(savedInstanceState);
        return view;
    }
    private void initView(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadRootFragment(R.id.fl_first_container, ViewPagerFragment.newInstance());
        }
    }
    @Override
    protected void initLazyView(@Nullable Bundle savedInstanceState) {

    }

}
