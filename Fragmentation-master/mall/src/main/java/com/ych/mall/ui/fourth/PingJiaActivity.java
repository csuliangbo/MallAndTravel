package com.ych.mall.ui.fourth;

import android.os.Bundle;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

/**
 * Created by Administrator on 2016/11/1.
 */
@EActivity(R.layout.activity_pingjia)
public class PingJiaActivity extends BaseActivity {
    private String goodsId;//商品Id
    private String orderId;//订单Id
    private String orderNum;//订单号

    @AfterViews
    void init() {
        Bundle bundle = getIntent().getExtras();
        goodsId = bundle.getString("GoodsId");
        orderId = bundle.getString("OrderId");
        orderNum = bundle.getString("OrderNum");
    }
}
