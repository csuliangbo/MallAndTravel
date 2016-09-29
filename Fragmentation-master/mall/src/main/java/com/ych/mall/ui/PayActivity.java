package com.ych.mall.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ych.mall.R;
import com.ych.mall.bean.CreateOrderBean;
import com.ych.mall.bean.GoodsDetailBean;
import com.ych.mall.bean.PayBean;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.TravelRecverBean;
import com.ych.mall.event.AddressEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.HttpModel;
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
import com.ych.mall.utils.Tools;
import com.ych.mall.widget.ClearEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import org.json.JSONObject;


import java.util.ArrayList;
import java.util.HashMap;
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
    @ViewById(R.id.cb_pay_alipay)
    CheckBox cbPayAlipay;
    @ViewById(R.id.cb_pay_weixin)
    CheckBox cbPayWeixin;
    @ViewById(R.id.ll_pay_weixin)
    LinearLayout llPayWeixin;
    @ViewById(R.id.addressLayout)
    LinearLayout addressLayout;
    @ViewById(R.id.ll_contact)
    LinearLayout llContact;
    @ViewById(R.id.tv_contact_name)
    TextView tvContactName;
    @ViewById(R.id.tv_contact_phone)
    TextView tvContactPhone;

    private String cart_id;
    private String goods_id;
    private boolean isPayNow = false;
    private Double totalPrice = 0.0D;
    private Double payPrice = 0.0D;
    private Double fanli_jifen;
    private String date;
    private boolean isTravel = false;
    //收货地址
    private String address;
    //收货人
    private String realName;

    //旅游
    private String number;
    private String mobile;
    private String goodTitle;
    private String jifenA = "";
    private String jifenB = "";
    private double jifenATotal;
    private double jifenBTotal;
    private String childrenPrice = "";
    private String childrenNum = "";
    private String adultPrice = "";
    private String adultNum = "";
    private String jifenTravelTotal = "";
    private boolean isPay = false;

    @Click
    public void addressLayout() {
        startActivity(new Intent(this, AddressActivity_.class).putExtra("address", true));
    }

    @Click
    public void ll_contact() {
        Bundle bundle = new Bundle();
        bundle.putString("ContactName", tvContactName.getText().toString());
        bundle.putString("ContactPhone", tvContactPhone.getText().toString());
        startActivity(new Intent(this, ContactActivity_.class).putExtras(bundle));
    }

    @Click
    void onBack() {
        finish();
    }

    @Click
    void onPay() {
        if (!isPay) {
            createOrder();
        } else {
            TOT("你已经创建了订单，请到订单页面进行支付");
        }
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
                        finish();
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
        EventBus.getDefault().register(this);
        goods_id = getIntent().getExtras().getString(KV.GOODS_ID);
        Bundle bundle = getIntent().getExtras();
        goods_id = bundle.getString(KV.GOODS_ID);
        if (bundle.getInt("TYPE") == GoodsFragment.TYPE_TRAVEL) {
            isTravel = true;
            date = bundle.getString("Date");
            childrenNum = bundle.getString("ChildrenNum");
            childrenPrice = bundle.getString("ChildrenPrice");
            if (childrenPrice == null) {
                childrenNum = "0";
                childrenPrice = "0";
            }
            adultNum = bundle.getString("AdultNum");
            adultPrice = bundle.getString("AdultPrice");
            llA.setVisibility(View.GONE);
            addressLayout.setVisibility(View.GONE);
            llContact.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(goods_id)) {
            isPayNow = true;
        }
        cart_id = getIntent().getExtras().getString(KV.CART_ID);
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
        initClearEdittext();
        initCheckbox();
    }

    /**
     * 监听积分输入框
     */
    void initClearEdittext() {
        cetA.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(cetA.getText().toString())) {
                    return;
                }
                double jifenA = Double.parseDouble(cetA.getText().toString());
                if (jifenA > jifenATotal) {
                    TOT("A账号的积分余额不足");
                } else {

                    tvPayPrice.setText("应付：￥" + Tools.sub(totalPrice, jifenA));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(cetA.getText().toString())) {
                    if (TextUtils.isEmpty(cetB.getText().toString())) {
                        tvPayPrice.setText("应付：￥" + totalPrice);
                    } else {
                        tvPayPrice.setText("应付：￥" + Tools.sub(totalPrice, Double.parseDouble(cetB.getText().toString())));

                    }
                }
            }
        });
        cetB.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(cetB.getText().toString())) {
                    return;
                }
                double jifenB = Double.parseDouble(cetB.getText().toString());
                if (jifenB > jifenBTotal) {
                    TOT("B账号的积分余额不足");
                } else {
                    if (TextUtils.isEmpty(cetA.getText().toString())) {
                        tvPayPrice.setText("应付：￥" + Tools.sub(totalPrice, jifenB));

                    } else {
                        tvPayPrice.setText("应付：￥" + Tools.sub(Tools.sub(totalPrice,
                                Double.parseDouble(cetA.getText().toString())), jifenB));

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(cetB.getText().toString())) {
                    if (TextUtils.isEmpty(cetA.getText().toString())) {
                        tvPayPrice.setText("应付：￥" + totalPrice);
                    } else {

                        tvPayPrice.setText("应付：￥" + Tools.sub(totalPrice, Double.parseDouble(cetA.getText().toString())));
                    }
                }
            }
        });
    }

    /**
     * 监听支付方式的选择
     */
    void initCheckbox() {
        cbPayAlipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbPayWeixin.setChecked(false);
                } else {
                    cbPayWeixin.setChecked(true);
                }
            }
        });
        cbPayWeixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbPayAlipay.setChecked(false);
                } else {
                    cbPayAlipay.setChecked(true);
                }
            }
        });
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
        holder.setVisible(R.id.ll_taocan, View.VISIBLE);
        holder.setText(R.id.tv_taocan, t.getTaocan_name());

        totalPrice = totalPrice + Double.parseDouble(t.getPrice_new());
        tvTotalPrice.setText(totalPrice + "");
        if (TextUtils.isEmpty(t.getFanli_jifen())) {
            holder.setVisible(R.id.ll_fanli, View.GONE);
        }
        holder.loadImg(PayActivity.this, R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        tvA.setText("A账户：" + t.getAdd_jf_limit());
        tvB.setText("B账户：" + t.getAdd_jf_currency());
        jifenATotal = Double.parseDouble(t.getAdd_jf_limit());
        jifenBTotal = Double.parseDouble(t.getAdd_jf_currency());
        tvPayPrice.setText("应付：" + totalPrice);
        fanli_jifen = Double.parseDouble(t.getFanli_jifen());
        address = t.getAddress();
        if (t.getGoods_num() == null) {
            number = 1 + "";
        } else {
            number = t.getGoods_num();
        }
        holder.setText(R.id.tv_number, number);
        cart_id = t.getCart_id();
        realName = t.getRealname();
        mobile = t.getMobile();
        goodTitle = t.getTitle();
        tvRealName.setText("收货人：" + realName);
        tvAddress.setText(address);
    }

    /**
     * 创建订单
     */
    void createOrder() {
        payPrice = totalPrice;
        if (!TextUtils.isEmpty(cetA.getText().toString())) {
            if (Double.parseDouble(cetA.getText().toString()) > jifenATotal) {
                TOT("A账号的积分余额不足");
                cetA.setText("");
                return;
            }

            totalPrice = Tools.sub(totalPrice, Double.parseDouble(cetA.getText().toString()));
            jifenA = cetA.getText().toString() + "";
        }
        if (!TextUtils.isEmpty(cetB.getText().toString())) {
            if (Double.parseDouble(cetB.getText().toString()) > jifenBTotal) {
                TOT("B账号的积分余额不足");
                cetB.setText("");
                return;
            }
            totalPrice = Tools.sub(totalPrice, Double.parseDouble(cetB.getText().toString()));
            jifenB = cetB.getText().toString() + "";
        }
        tvPayPrice.setText("￥" + totalPrice);
        if (isTravel) {
            MallAndTravelModel.createTourOrder(createCallback, payPrice + "", totalPrice + "", realName,
                    mobile, number, jifenTravelTotal, goods_id, jifenB, date, childrenNum, childrenPrice, adultNum, adultPrice);
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
            CreateOrderBean bean = Http.model(CreateOrderBean.class, response);
            if (bean.getCode().equals("200")) {
                isPay = true;
                UserInfoModel.pay(payCallBack, bean.getData().get(0), 0.01 + "", goodTitle);
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
                if (cbPayAlipay.isChecked()) {
                    payInerface(bean.getData());
                } else {
                    payWeixin();
                }
                TOT(bean.getMessage());
                //payChoosePopupwindow(bean.getData());
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
            Double childrenPriceTotal = 0.00d;
            holder.setVisible(R.id.ll_number, View.VISIBLE);
            holder.setText(R.id.name, t.getGoods().getTitle());
            holder.setText(R.id.price, "￥" + t.getGoods().getPrice_new());
            holder.setText(R.id.num, t.getGoods().getFanli_jifen());
            holder.setText(R.id.tv_adult_number, adultNum);
            holder.setText(R.id.tv_adult_price, adultPrice);
            Double adultPriceTotal = Integer.parseInt(adultNum) * Double.parseDouble(adultPrice);
            holder.setText(R.id.tv_adult_total_price, adultPriceTotal + "");
            if (childrenPrice == "0") {
                holder.setVisible(R.id.ll_children, View.GONE);
            } else {
                holder.setText(R.id.tv_children_number, childrenNum);
                holder.setText(R.id.tv_children_price, childrenPrice);
                childrenPriceTotal = Integer.parseInt(childrenNum) * Double.parseDouble(childrenPrice);
                holder.setText(R.id.tv_children_total_price, childrenPriceTotal + "");
            }

            holder.setText(R.id.tv_date, t.getGoods().getChufa_date());
            holder.setText(R.id.tv_address, t.getGoods().getChufa_address());
            int numTotal = Integer.parseInt(childrenNum) + Integer.parseInt(adultNum);
            holder.setText(R.id.tv_jifen_num, numTotal + "");
            holder.setText(R.id.tv_jifen, t.getGoods().getFanli_jifen());
            jifenTravelTotal = Double.parseDouble(t.getGoods().getFanli_jifen()) * numTotal + "";
            holder.setText(R.id.tv_jifen_total, jifenTravelTotal);
            totalPrice = Tools.add(Tools.add(totalPrice, adultPriceTotal), childrenPriceTotal);
            tvTotalPrice.setText(totalPrice + "");
            holder.loadImg(PayActivity.this, R.id.pic, Http.GOODS_PIC_URL + t.getGoods().getPic_url());
            tvA.setText("A账户：" + t.getJifen().getAdd_jf_limit());
            tvB.setText("B账户：" + t.getJifen().getAdd_jf_currency());
            jifenATotal = Double.parseDouble(t.getJifen().getAdd_jf_limit());
            jifenBTotal = Double.parseDouble(t.getJifen().getAdd_jf_currency());
            tvPayPrice.setText("应付：￥" + totalPrice);
            number = 1 + "";
            fanli_jifen = Double.parseDouble(t.getGoods().getFanli_jifen());
            address = t.getAddress().getAddress();
            realName = t.getAddress().getRealname();
            mobile = t.getAddress().getMobile();
            goodTitle = t.getGoods().getTitle();
            tvRealName.setText("收货人：" + realName);
            tvAddress.setText(address);
            tvContactPhone.setText(t.getAddress().getMobile());
            tvContactName.setText(t.getAddress().getRealname());
        }
    }

    private void payChoosePopupwindow(final String orderInfo) {

        View popupWindow_view = getLayoutInflater().inflate(R.layout.item_popupwindow_pay, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(llA, Gravity.BOTTOM, 0, 0);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        LinearLayout llWeixin = (LinearLayout) popupWindow_view.findViewById(R.id.ll_weixin);
        LinearLayout llAlipay = (LinearLayout) popupWindow_view.findViewById(R.id.ll_alipay);
        llWeixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                payWeixin();
            }
        });
        llAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                payInerface(orderInfo);
            }
        });
        popupWindow_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }
        });
    }

    /**
     * 微信支付
     */
    private IWXAPI api;

    private void payWeixin() {
        api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
        String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
        TOT("获取订单中...");
        HashMap<String, String> map = new HashMap<>();
        HttpModel.newInstance(url).post(map, callback);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(AddressEvent e) {
        realName = e.getName();
        address = e.getAddress();
        mobile = e.getPhone();
        tvRealName.setText("收货人：" + realName + "\t\t\t" + e.getPhone());
        tvAddress.setText(address);
        //旅游联系方式
        tvContactName.setText(e.getName());
        tvContactPhone.setText(e.getPhone());


    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            try {

                Log.e("get server pay params:", response);
                JSONObject json = new JSONObject(response);
                if (null != json && !json.has("retcode")) {
                    PayReq req = new PayReq();
                    //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                    req.appId = json.getString("appid");
                    req.partnerId = json.getString("partnerid");
                    req.prepayId = json.getString("prepayid");
                    req.nonceStr = json.getString("noncestr");
                    req.timeStamp = json.getString("timestamp");
                    req.packageValue = json.getString("package");
                    req.sign = json.getString("sign");
                    //req.extData = "app data"; // optional
                    TOT("正常调起支付");
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    api.sendReq(req);
                } else {
                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                    TOT("返回错误" + json.getString("retmsg"));
                }

            } catch (Exception e) {
                Log.e("PAY_GET", "异常：" + e.getMessage());
                TOT("异常：" + e.getMessage());
            }
        }
    };

}
