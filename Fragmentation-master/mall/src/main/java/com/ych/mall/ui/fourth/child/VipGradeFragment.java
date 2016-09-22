package com.ych.mall.ui.fourth.child;

import android.os.Bundle;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by Administrator on 2016/9/22.
 */
@EFragment(R.layout.fragment_vip_grade)
public class VipGradeFragment extends BaseFragment {
    public static VipGradeFragment newInstance() {
        Bundle bundle = new Bundle();
        VipGradeFragment fragment = new VipGradeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }
}
