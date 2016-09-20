package com.ych.mall.ui;

import android.app.Activity;
import android.content.Intent;

import com.ych.mall.R;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

/**
 * Created by ych on 2016/9/6.
 */
@EActivity(R.layout.activity_pay)
public class PayActivity extends Activity {

    @Click
    public void addressLayout(){
        startActivity(new Intent(this,AddressActivity_.class));
    }
}
