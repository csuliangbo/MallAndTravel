package com.ych.mall;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.ych.mall.utils.SharedPreferencesUtil;

/**
 * Created by ych on 2016/9/12.
 * Email : a18008112518@hotmail.com 叶昌洪 THX
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        //微信 appid appsecret
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        // QQ和Qzone appid appkey
        SharedPreferencesUtil util = new SharedPreferencesUtil();
        util.init(this);
    }
}
