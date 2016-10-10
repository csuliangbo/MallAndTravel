package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * 关于我们
 */
@EFragment(R.layout.fragment_about_mall)
public class AboutUsFragment extends BaseFragment {
    public static AboutUsFragment newInstance() {
        Bundle bundle = new Bundle();
        AboutUsFragment fragment = new AboutUsFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById(R.id.tiTitle)
    TextView tiTitle;

    @AfterViews
    void init() {
        tiTitle.setText("关于我们");
    }

    @Click
    void onBack() {
        back();
    }

}
