package com.ych.fragmenttest;

import android.util.Log;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/2.
 */
public class HttpModel {
    final String TAG="httpmodel";
    String url;

    public HttpModel(String url) {
        this.url = url;
    }

    public void Post(Map<String, String> p) {
        Log.i(TAG, "onResponse: 执行" );
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key : p.keySet()) {
            builder.addParams(key, p.get(key));
        }
        builder.build().buildCall(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i(TAG, "onResponse: err" );
            }

            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG, "onResponse: "+response);
            }
        });
    }

    public void file(Map<String, String> p, Map<String, String> files) {
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key : p.keySet()) {
            builder.addParams(key, p.get(key));
        }
        for (String key : files.keySet()) {
            File file = new File(files.get(key));
            builder.addFile(key, file.getName(), file);
        }
        builder.build().buildCall(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {

            }
        });
    }
}
