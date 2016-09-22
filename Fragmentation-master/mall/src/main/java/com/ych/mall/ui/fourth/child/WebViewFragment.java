package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.webkit.WebView;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Administrator on 2016/9/22.
 */
@EFragment(R.layout.fragment_webview)
public class WebViewFragment extends BaseFragment {
    @ViewById(R.id.webview)
    WebView webView;
    private String url;

    public static WebViewFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.URL, url);
        WebViewFragment fragment = new WebViewFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void init() {
        url = getArguments().getString(KV.URL);
        TOT(url);
       // webView.loadUrl(url);
    }
}
