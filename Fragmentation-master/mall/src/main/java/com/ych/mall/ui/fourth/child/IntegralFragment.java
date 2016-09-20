package com.ych.mall.ui.fourth.child;

import android.os.Bundle;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by ych on 2016/9/11.
 */
@EFragment(R.layout.fragment_my_account_score)
public class IntegralFragment extends BaseFragment{

    public static IntegralFragment newInstance(){
        Bundle bundle=new Bundle();
        IntegralFragment fragment=new IntegralFragment_();
        fragment.setArguments(bundle);
        return fragment;

    }

}
