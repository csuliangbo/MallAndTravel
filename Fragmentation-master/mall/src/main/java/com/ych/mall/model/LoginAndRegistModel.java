package com.ych.mall.model;

import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

/**
 * Created by ych on 2016/9/12.
 */
public class LoginAndRegistModel {
    static String URL = Http.SERVER_URL + "api.php?action=";


    static String REGIST_URL = URL + "user_reg";

    //注册
    public static void regist(String mobile, String sms_code, String idcard, String realname, String password, String id, StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("sms_code", sms_code);
        map.put("idcard", idcard);
        map.put("realname", realname);
        map.put("id", id);
        map.put("password", password);
        HttpModel hp = HttpModel.newInstance(REGIST_URL);
        hp.post(map, callback);

    }

    static String CODE_URL = URL + "get_sms";

    //获取验证码
    public static void code(String mobile) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        HttpModel hp = HttpModel.newInstance(CODE_URL);
        hp.post(map, null);
    }

    static String LOGIN_URL = URL + "user_login";

    //登录
    public static void login(String mobile, String pwd, StringCallback callback) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("password", pwd);
        HttpModel hp = HttpModel.newInstance(LOGIN_URL);
        hp.post(map, callback);
    }

    static String FORGET_URl=URL+"rest_password";

    //密码重置
    public static void rest(String mobile,String pwd,String code,StringCallback callback){
        HashMap<String,String> map=new HashMap<>();
        map.put("mobile",mobile);
        map.put("password",pwd);
        map.put("sms_code",code);
        HttpModel.newInstance(FORGET_URl).post(map,callback);
    }
}
