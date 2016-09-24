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
import com.ych.mall.bean.CreateOrderBean;
import com.ych.mall.bean.GoodsDetailBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.PayBean;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.SearchBean;
import com.ych.mall.bean.SearchTravelBean;
import com.ych.mall.bean.ShopCarBean;
import com.ych.mall.bean.TravelRecverBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.UserInfoModel;
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

import okhttp3.Call;

/**
 * Created by ych on 2016/9/6.
 */
@EActivity(R.layout.activity_pay)
public class PayActivity extends BaseActivity implements RecyclerViewModel.RModelListener<PayBean.PayData> {
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;


    @ViewById(R.id.rlv_pay)
    RecyclerView rlvPay;
    @ViewById(R.id.tv_real_name)
    TextView tvRealName;
    @ViewById(R.id.tv_address)
    TextView tvAddress;
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
    @ViewById(R.id.tv_pay_price)
    TextView tvPayPrice;

    private String cart_id;
    private String goods_id;
    private boolean isPayNow = false;
    private Double totalPrice = 0.0D;
    private Double payPrice = 0.0D;
    private Double fanli_jifen;
    private String date;
    private boolean isTravel = false;
    private String address;
    private String number;
    private String realName;
    private String mobile;
    private String goodTitle;
    private String jifenA = "";
    private String jifenB = "";

    @Click
    public void addressLayout() {
        startActivity(new Intent(this, AddressActivity_.class));
    }

    @Click
    void onBack() {
        finish();
    }

    @Click
    void onPay() {
        createOrder();
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
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
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
    RecyclerViewNormalModel<TravelRecverBean.TravelRecverData> rvmTravel;

    @AfterViews
    void init() {
        goods_id = getIntent().getExtras().getString(KV.GOODS_ID);
        Bundle bundle = getIntent().getExtras();
        goods_id = bundle.getString(KV.GOODS_ID);
        if (bundle.getInt("TYPE") == GoodsFragment.TYPE_TRAVEL) {
            isTravel = true;
            date = bundle.getString("Date");
            llA.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(goods_id)) {
            isPayNow = true;
        }
        cart_id = getIntent().getExtras().getString(KV.CART_ID);
        Log.e("KTY", " cart ID  " + cart_id + "  goods id  " + goods_id + "  data " + date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(OrientationHelper.VERTICAL);
        if (!isTravel) {
            rvm = new RecyclerViewNormalModel<>(this, this, rlvPay, R.layout.item_goods_list);
            rvm.init(layoutManager);
        } else {
            rvmTravel = new RecyclerViewNormalModel<>(this, new TravelRecver(), rlvPay, R.layout.item_goods_list);
            rvmTravel.init(layoutManager1);
        }
    }

    @Override
    public void getData(StringCallback callback, int page) {
        if (isPayNow) {
            MallAndTravelModel.payNow(callback, goods_id);

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
        holder.setText(R.id.num, t.getFanli_jifen());
        totalPrice = totalPrice + Double.parseDouble(t.getPrice_new());
        tvTotalPrice.setText(totalPrice + "");
        if (TextUtils.isEmpty(t.getFanli_jifen())) {
            holder.setVisible(R.id.ll_fanli, View.GONE);
        }
        holder.loadImg(PayActivity.this, R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        tvA.setText("A账户：" + t.getAdd_jf_limit());
        tvB.setText("B账户：" + t.getAdd_jf_currency());
        tvPayPrice.setText("应付：" + totalPrice);
        fanli_jifen = Double.parseDouble(t.getFanli_jifen());
        address = t.getAddress();
        if (t.getGoods_num() == null) {
            number = 1 + "";
        } else {
            number = t.getGoods_num();
        }
        cart_id = t.getCart_id();
        realName = t.getRealname();
        mobile = t.getMobile();
        goodTitle = t.getTitle();
        tvRealName.setText("收货人：" + realName);
        tvAddress.setText(address);
    }

    void createOrder() {
        payPrice = totalPrice;
        if (!TextUtils.isEmpty(cetA.getText().toString())) {
            totalPrice = totalPrice - Double.parseDouble(cetA.getText().toString());
            jifenA = cetA.getText().toString() + "";
        }
        if (!TextUtils.isEmpty(cetB.getText().toString())) {
            totalPrice = totalPrice - Double.parseDouble(cetB.getText().toString());
            jifenB = cetB.getText().toString() + "";
        }
        tvPayPrice.setText(totalPrice + "");
        // Log.e("KTY", " " + payPrice + " " + totalPrice + " " + address + " " + realName + " " + mobile + " " + number + " " + fanli_jifen + " " + cart_id);
        if (isTravel) {
            //Log.e("KTY", " " + payPrice + " " + totalPrice + " " + address + " " + realName + " " + mobile + " " + number + " " + fanli_jifen + " " + goods_id + " " + jifenA + " " + jifenB + " " + date);
            MallAndTravelModel.createTourOrder(createCallback, payPrice + "", totalPrice + "", address, realName,
                    mobile, number, fanli_jifen + "", goods_id, jifenA, jifenB, date);
        } else {
            MallAndTravelModel.createOrder(createCallback, payPrice + "", totalPrice + "", address, realName,
                    mobile, number, fanli_jifen + "", cart_id);
        }


    }


    //创建订单
    private StringCallback createCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e("KTY  order ", response);
            CreateOrderBean bean = Http.model(CreateOrderBean.class, response);
            if (bean.getCode().equals("200")) {
                UserInfoModel.pay(payCallBack, bean.getData().get(0), totalPrice + "", goodTitle);
            }
        }
    };
    //支付
    private StringCallback payCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            PayRequestBean bean = Http.model(PayRequestBean.class, response);
            Log.e("KTY pay", response);
            if (bean.getCode().equals("200")) {
                payInerface(bean.getData());
            }
        }
    };


    class TravelRecver implements RecyclerViewModel.RModelListener<TravelRecverBean.TravelRecverData> {

        @Override
        public void getData(StringCallback callback, int page) {
            MallAndTravelModel.travelReseve(callback, goods_id, date);
        }

        @Override
        public void onErr(int state) {
            TOT("网络连接失败");
        }

        @Override
        public List<TravelRecverBean.TravelRecverData> getList(String str) {
            TravelRecverBean bean = Http.model(TravelRecverBean.class, str);
            if (bean.getCode().equals("200")) {
                List<TravelRecverBean.TravelRecverData> datas = new ArrayList<TravelRecverBean.TravelRecverData>();
                datas.add(bean.getData());
                return datas;
            }
            return null;
        }

        @Override
        public void covert(YViewHolder holder, TravelRecverBean.TravelRecverData t) {
            holder.setText(R.id.name, t.getGoods().getTitle());
            holder.setText(R.id.price, "￥" + t.getGoods().getPrice_new());
            holder.setText(R.id.num, t.getGoods().getFanli_jifen());
            totalPrice = totalPrice + Double.parseDouble(t.getGoods().getPrice_new());
            tvTotalPrice.setText(totalPrice + "");
            holder.loadImg(PayActivity.this, R.id.pic, Http.GOODS_PIC_URL + t.getGoods().getPic_url());
            tvA.setText("A账户：" + t.getJifen().getAdd_jf_limit());
            tvB.setText("B账户：" + t.getJifen().getAdd_jf_currency());
            tvPayPrice.setText("应付：" + totalPrice);
            number = 1 + "";
            fanli_jifen = Double.parseDouble(t.getGoods().getFanli_jifen());
            address = t.getAddress().getAddress();
            realName = t.getAddress().getRealname();
            mobile = t.getAddress().getMobile();
            goodTitle = t.getGoods().getTitle();
            tvRealName.setText("收货人：" + realName);
            tvAddress.setText(address);
        }
    }
}
