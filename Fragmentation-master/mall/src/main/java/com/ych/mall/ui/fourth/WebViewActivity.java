package com.ych.mall.ui.fourth;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/9/22.
 */
@EActivity(R.layout.fragment_webview)
public class WebViewActivity extends BaseActivity {
    @ViewById(R.id.webview)
    WebView webView;
    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    private String url;

    @Click
    void onBack() {
        finish();
    }

    @AfterViews
    void init() {
        tiTitle.setText("网页");
        url = getIntent().getStringExtra(KV.URL);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
}
