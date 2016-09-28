package com.ych.mall.ui.first.child.childpager;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * Created by ych on 2016/9/5.
 */
@EFragment(R.layout.fragment_detail)
public class DetailFragment extends BaseFragment {
    @ViewById(R.id.webView)
    WebView wv;
    String id;
    int type;
    String goodsUrl = "http://www.zzumall.com/index.php/Mobile/Goods/goods_detail_m.html?id=";
    String travelUrl = "http://www.zzumall.com/index.php/Mobile/Tourism/tourism_detail_m.html?id=";
    String url="https://www.baidu.com/s?tn=80035161_2_dg&wd=res_atuo";

    public static DetailFragment newInstance(int type, String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.ID, id);
        bundle.putInt(KV.TYPE, type);
        DetailFragment fragment = new DetailFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    void init() {
        type = getArguments().getInt(KV.TYPE);
        id = getArguments().getString(KV.ID);
        if (type == GoodsFragment.TYPE_GOODS)
            url = goodsUrl + id;
        else
            url = travelUrl + id;
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDisplayZoomControls(false);//
        wv.loadUrl(url);

    }
}

