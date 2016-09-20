package com.ych.mall.ui.fourth.child;

import android.os.Bundle;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;

import org.androidannotations.annotations.EFragment;

/**
 * Created by ych on 2016/9/11.
 */
@EFragment(R.layout.fragment_ticket)
public class TicketFragment extends BaseFragment {

    public static TicketFragment newInstance() {
        Bundle bundle = new Bundle();
        TicketFragment fragment = new TicketFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }
}
