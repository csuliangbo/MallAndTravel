package com.ych.mall.utils;

import android.nfc.Tag;
import android.util.Log;

/**
 * Created by ych on 2016/9/12.
 */
public class UserCenter {

    String TAG="usercenter";
    public static String USER_ID = "user_id";
    public static String USER_GRADE = "user_grade";
    public static String USER_PHONE = "user_phone";
    public static String TOURIST = "游客";
    public static String VIP = "会员";
    public static String MEMBER = "合伙";


    private static UserCenter instance;

    public static UserCenter getInstance() {
        if (instance == null)
            instance = new UserCenter();
        return instance;
    }

    public boolean isLoggin() {
        Log.i(TAG,getCurrentUserId());
        if (SharedPreferencesUtil.getString(USER_ID).equals(""))
            return false;
        else
            return true;
    }

    public String getCurrentUserId() {
        return SharedPreferencesUtil.getString(USER_ID);
    }

    public void setCurrentUserId(String id) {
        SharedPreferencesUtil.putString(USER_ID, id);
    }

    public String getCurrentUserGrade() {
        return SharedPreferencesUtil.getString(USER_GRADE);
    }

    public void setUserGrade(String grade) {
        SharedPreferencesUtil.putString(USER_GRADE, grade);
    }

    public String getCurrentUserPhone() {
        return SharedPreferencesUtil.getString(USER_PHONE);
    }

    public void setCurrentUserPhone(String phone) {
        SharedPreferencesUtil.putString(USER_PHONE, phone);
    }

    public boolean isTourist(){
        Log.i(TAG,getCurrentUserGrade());
        if (getCurrentUserGrade().equals("")||getCurrentUserGrade().equals(TOURIST))
            return true;
        else
            return false;

    }

    public void out(){
         setCurrentUserId("");
        setUserGrade("");
        setCurrentUserPhone("");

    }
}
