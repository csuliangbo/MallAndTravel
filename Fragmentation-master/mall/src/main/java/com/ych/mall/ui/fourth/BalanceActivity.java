package com.ych.mall.ui.fourth;

import android.app.Activity;
import android.widget.TextView;

import com.ych.mall.R;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ych on 2016/9/9.
 */
@EActivity(R.layout.fragment_my_account_balance)
public class BalanceActivity extends Activity {

    @ViewById
    TextView tiTitle;
    @Click
    void onBack(){
        finish();
    }
    @AfterViews
    void initView(){
        tiTitle.setText("账户余额");
    }
}
