package com.ych.mall.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ych.mall.R;
import com.ych.mall.bean.GoodsDetailBean;
import com.ych.mall.bean.PayBean;
import com.ych.mall.bean.SearchBean;
import com.ych.mall.bean.SearchTravelBean;
import com.ych.mall.bean.ShopCarBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.PayResult;
import com.ych.mall.ui.first.child.childpager.GoodsFragment;
import com.ych.mall.utils.KV;
import com.ych.mall.widget.ClearEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ych on 2016/9/6.
 */
@EActivity(R.layout.activity_pay)
public class PayActivity extends BaseActivity implements RecyclerViewModel.RModelListener<PayBean.PayData> {
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;


    @ViewById(R.id.rlv_pay)
    RecyclerView rlvPay;
    @ViewById(R.id.tv_A)
    TextView tvA;
    @ViewById(R.id.tv_B)
    TextView tvB;
    @ViewById(R.id.tv_total_price)
    TextView tvTotalPrice;
    @ViewById(R.id.ll_a)
    LinearLayout llA;
    @ViewById(R.id.ll_b)
    LinearLayout llB;
    @ViewById(R.id.cet_A)
    ClearEditText cetA;
    @ViewById(R.id.cet_B)
    ClearEditText cetB;

    private String cart_id;
    private String goods_id;
    private boolean isPayNow = false;
    private Double totalPrice = 0.0;
    private Double fanli_jifen;
    private boolean isTravel = false;

    @Click
    public void addressLayout() {
        startActivity(new Intent(this, AddressActivity_.class));
    }

    @Click
    void onPay() {
        payInerface("");
    }

    /**
     * 支付宝支付
     */
    private void payInerface(final String orderInfo) {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        TOT("支付成功");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        TOT("支付失败");
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        TOT("授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()));
                    } else {
                        // 其他状态值则为授权失败
                        TOT("授权失败" + String.format("authCode:%s", authResult.getAuthCode()));

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };
    RecyclerViewNormalModel<GoodsDetailBean.GoodsDetailData> rvm;

    @AfterViews
    void init() {
        goods_id = getIntent().getExtras().getString(KV.GOODS_ID);
        Bundle bundle = getIntent().getExtras();
        goods_id = bundle.getString(KV.GOODS_ID);
        if (bundle.getInt("TYPE") == GoodsFragment.TYPE_TRAVEL) {
            isTravel = true;
            llA.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(goods_id)) {
            isPayNow = true;
        }
        cart_id = getIntent().getExtras().getString(KV.CART_ID);
        Log.e("KTY", " cart ID  " + cart_id + "  goods id  " + goods_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        rvm = new RecyclerViewNormalModel<>(this, this, rlvPay, R.layout.item_goods_list);
        rvm.init(layoutManager);
    }

    @Override
    public void getData(StringCallback callback, int page) {
        if (isPayNow) {
            if (isTravel) {
                MallAndTravelModel.travelReseve(callback, goods_id, "");
            } else {
                MallAndTravelModel.payNow(callback, goods_id);
            }

        } else {
            MallAndTravelModel.settelAccounts(callback, cart_id);
        }
    }

    @Override
    public void onErr(int state) {
        TOT("网络连接失败");
    }

    @Override
    public List<PayBean.PayData> getList(String str) {
        Log.e("KTY", str);
        PayBean bean = Http.model(PayBean.class, str);
        if (bean.getCode().equals("200")) {
            return bean.getData();
        }
        return null;
    }

    @Override
    public void covert(YViewHolder holder, PayBean.PayData t) {
        holder.setText(R.id.name, t.getTitle());
        holder.setText(R.id.price, "￥" + t.getPrice_new());
        totalPrice = totalPrice + Double.parseDouble(t.getPrice_new());
        tvTotalPrice.setText(totalPrice + "");
        holder.setVisible(R.id.ll_fanli, View.GONE);
        holder.loadImg(PayActivity.this, R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        tvA.setText("A账户：" + t.getAdd_jf_limit());
        tvB.setText("B账户：" + t.getAdd_jf_currency());
        fanli_jifen = Double.parseDouble(t.getFanli_jifen());
    }

    void initPay() {
        totalPrice = totalPrice - Double.parseDouble(cetA.getText().toString())
                - Double.parseDouble(cetB.getText().toString());

    }
}
