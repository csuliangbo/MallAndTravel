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
      loadRootFragment(R.id.home_container,FragmentHome.getInstance());

    }

}
