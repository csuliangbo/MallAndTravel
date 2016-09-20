package com.ych.mall.utils;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

/**
 * Created by ych on 2016/9/2.
 */
public class HttpModel {
    static String url;
    static HttpModel instances;

    public static HttpModel load(String URL) {
        url = URL;
        if (instances == null)
            instances = new HttpModel();
        return instances;
    }

    public void post(Map<String, String> p, StringCallback callback) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key : p.keySet()) {
            builder.addParams(key, p.get(key));
        }
        builder.build().execute(callback);
    }
 
    public void file(Map<String, String> p, Map<String, String> files, StringCallback callback) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key : p.keySet()) {
            builder.addParams(key, p.get(key));
        }
        for (String key : files.keySet()) {
            File file = new File(files.get(key));
            builder.addFile(key, file.getName(), file);
        }
        builder.build().execute(callback);
    }
}
