package com.ych.mall;

import android.app.Application;
import android.util.Log;

import com.umeng.socialize.PlatformConfig;
import com.ych.mall.utils.SharedPreferencesUtil;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by ych on 2016/9/12.
 * Email : a18008112518@hotmail.com 叶昌洪 THX
 */
public class MyApp extends Application {
    public static boolean isRelease = false;
    private static final String TAG = "JPush";
    public static boolean isPayActivity = true;
    public static String REGISTRATION_ID;

    @Override
    public void onCreate() {
        super.onCreate();
        PlatformConfig.setWeixin("wxc60cf9d8efdbbd50", "2e12d8c57e9a7066b92d3c83c83c1400");
        //微信 appid appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        // QQ和Qzone appid appkey
        SharedPreferencesUtil util = new SharedPreferencesUtil();
        util.init(this);
        //极光推送
        JPushInterface.setDebugMode(false);//如果时正式版就改成false
        JPushInterface.init(this);
        REGISTRATION_ID = JPushInterface.getRegistrationID(getApplicationContext());
        Log.e("KTY****", REGISTRATION_ID + "");
    }
}
