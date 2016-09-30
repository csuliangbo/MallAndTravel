package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.LogisticsBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/21.
 */
@EFragment(R.layout.fragment_logistics)
public class LogisticsFragment extends BaseFragment {
    @ViewById(R.id.webview)
    WebView webView;
    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    String orderNum;
    String url = "http://www.zzumall.com/kuaidi/index.php?num=";

    public static LogisticsFragment newInstance(String orderNum) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.KUIDI, orderNum);
        LogisticsFragment fragment = new LogisticsFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Click
    void onBack() {
        back();
    }

    @AfterViews
    void init() {
        tiTitle.setText("查看物流");
        orderNum = getArguments().getString(KV.ORDER_NUM);
        if (orderNum == null) {
            TOT("没有订单号");
            return;
        }
        UserInfoModel.kuaidi(kuaidiCallback, orderNum);
    }

    //查看物流
    StringCallback kuaidiCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            LogisticsBean bean = Http.model(LogisticsBean.class, response);
            if (bean.getCode().equals("200")) {
                TOT("快递");
                List<LogisticsBean.LogisticsData> data = bean.getData();
                webView.loadUrl(url + data.get(0).getKuaidi_num());
            }
        }
    };
}
