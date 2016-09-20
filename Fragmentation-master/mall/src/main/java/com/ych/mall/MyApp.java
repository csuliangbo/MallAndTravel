package com.ych.mall;

import android.app.Application;

import com.ych.mall.utils.SharedPreferencesUtil;

/**
 * Created by ych on 2016/9/12.
 * Email : a18008112518@hotmail.com 叶昌洪 THX
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesUtil util=new SharedPreferencesUtil();
        util.init(this);
    }
}
