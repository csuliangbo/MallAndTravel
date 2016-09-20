package com.ych.fragmenttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import me.yokeyword.fragmentation.SupportActivity;
import okhttp3.Call;

public class MainActivity extends SupportActivity {
    final String TAG="httpmodel";
    String url="http://180.173.56.12:8099/WebAPI/AddArticle/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url="http://180.173.56.12:8099/WebAPI/AddArticle/";
        Map<String,String> p=new HashMap<String, String>();
        p.put("userCode","18008112518");
        p.put("title","okHttp Test");
        p.put("content","this is a test");
        p.put("files"," ");
       Post(p);


    }

    public void Post(Map<String, String> p) {
        Toast.makeText(MainActivity.this,"start",Toast.LENGTH_SHORT).show();
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        for (String key : p.keySet()) {
            builder.addParams(key, p.get(key));
        }
        builder.build().buildCall(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(MainActivity.this,"err",0).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Toast.makeText(MainActivity.this,response,0).show();
            }
        });
    }
}
