package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by ych on 2016/10/10.
 */
@EFragment(R.layout.fragment_webview)
public class ShareWebFragment extends BaseFragment {
    @ViewById(R.id.webview)
    ImageView webView;
    private String url;
    @ViewById
    TextView tiText, tiTitle;

    @Click
    void onBack() {
        back();
    }

    @Click
    void tiText() {
        umShare(url);
    }

    public static ShareWebFragment newInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.URL, url);
        ShareWebFragment fragment = new ShareWebFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void init() {
        url = getArguments().getString(KV.URL);
        Glide.with(this).load(url).into(webView);
        tiText.setText("分享");
        tiTitle.setText("我的二维码");
        tiText.setVisibility(View.VISIBLE);
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            TOT("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            TOT("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            TOT("分享取消了");
        }
    };

    private void umShare(String url) {
        UMImage image = new UMImage(getActivity(), R.drawable.icon_logo);//资源文件
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE
                };
        new ShareAction(getActivity()).setDisplayList(displaylist)
                .withText("我的主页")
                .withTitle("掌中游")
                .withMedia(image)
                .withTargetUrl(url)
                .setListenerList(umShareListener)
                .open();
    }
}
