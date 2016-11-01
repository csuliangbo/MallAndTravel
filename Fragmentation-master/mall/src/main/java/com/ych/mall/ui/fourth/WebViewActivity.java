package com.ych.mall.ui.fourth;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ych.mall.MainActivity_;
import com.ych.mall.R;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/9/22.
 */
@EActivity(R.layout.activity_web)
public class WebViewActivity extends BaseActivity {
    @ViewById(R.id.webview)
    WebView webView;
    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    @ViewById(R.id.progress_bar)
    ProgressBar myProgressBar;
    private String url;

    @Click
    void onBack() {
        finish();
    }

    @AfterViews
    void init() {
        tiTitle.setText("网页");
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        //设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        //支持js
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setSupportZoom(true);
        //设置背景颜色 透明
        webView.setBackgroundColor(Color.argb(0, 0, 0, 0));
        url = getIntent().getStringExtra(KV.URL);
        if (url.equals("http://kefu.easemob.com/webim/im.html?tenantId=29457")) {
            tiTitle.setText("客服");
        }
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    myProgressBar.setVisibility(View.GONE);
                } else {
                    myProgressBar.setProgress(newProgress);
                }

            }
        });
    }




}
