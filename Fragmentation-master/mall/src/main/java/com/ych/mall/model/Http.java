package com.ych.mall.model;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * Created by ych on 2016/9/12.
 */
public class Http {
    private static final String URL_TEST="http://115.159.146.202/api/";
    private static final String URL_RELEASE="www.zzumall.com/api";
    private static final String PIC_URL="http://www.zzumall.com/Public/Uploads/";
    public static final String AD_PIC_URL=PIC_URL+"ad/";
    public static final String GOODS_PIC_URL=PIC_URL+"goods/";
    public static final String CLASS_PIC_URL=PIC_URL+"class/";
    public static final String IMAGE_PIC_URL=PIC_URL+"image/";
    public static final String SERVER_URL=URL_TEST;

    public static <T> T model(Class<T> clazz, String json) {
        try {
            Gson gson = new Gson();
            T obj = gson.fromJson(json, clazz);
            return obj;
        } catch (JsonSyntaxException e) {

        }
        return null;
    }
}
