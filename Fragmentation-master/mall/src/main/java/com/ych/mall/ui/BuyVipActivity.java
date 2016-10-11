package com.ych.mall.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.ych.mall.R;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.CreateVipBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.PayBean;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.PayResult;
import com.ych.mall.bean.UpPayRequestBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.HttpModel;
import com.ych.mall.model.LoginAndRegistModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.UserCenter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/24.
 */
@EActivity(R.layout.activity_buy_vip)
public class BuyVipActivity extends BaseActivity {
    private String money;
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;
    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    @ViewById(R.id.tvLoading)
    TextView tvLoading;

    @Click
    void onBack() {
        finish();
    }

    @AfterViews
    void init() {
        tiTitle.setText("加盟");
    }

    @Click
    void onJoin() {
        tvLoading.setVisibility(View.VISIBLE);
        canBuyVip();
    }

    @Click
    void onQuit() {
        finish();
        UserCenter.getInstance().out();
        startActivity(new Intent(this, LoginActivity_.class));
    }

    boolean canBuyVip() {
        LoginAndRegistModel.canBuyVip(canBuyCallback);
        return false;
    }

    StringCallback canBuyCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            tvLoading.setText("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            final ParentBean bean = Http.model(PayBean.class, response);
            String code = bean.getCode();
            if (code.equals("41")) {
                TOT("已经具有会员资格,无需购买");
            } else if (code.equals("201")) {
                TOT("只能购买会员");
                LoginAndRegistModel.createVipOrder(buyVipCallback, 365 + "");
            } else if (code.equals("200")) {
                TOT("可以任意购买");
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        BuyVipActivity.this);
                builder.setTitle("请选择一下购买类型");
                builder.setSingleChoiceItems(new String[]{"会员", "合伙人"}, 0,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    money = 365.0 + "";
                                } else {
                                    money = 30000 + "";
                                    TOT("大额支付功能尚未开发");
                                    dialog.dismiss();
                                }
                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        LoginAndRegistModel.createVipOrder(buyVipCallback, money);
                        tvLoading.setVisibility(View.VISIBLE);

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else if (code.equals("42")) {
                TOT("上级不具有会员资格,不能进行购买.");
            }
        }
    };
    StringCallback buyVipCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            tvLoading.setText("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e("KEy 123", response);
            CreateVipBean bean = Http.model(CreateVipBean.class, response);
            String code = bean.getCode();
            if (code.equals("401")) {
                TOT("未定义的用户id");
            } else if (code.equals("402")) {
                TOT("购买金额错误");
            } else if (code.equals("403")) {
                TOT("未定义的购买金额");
            } else if (code.equals("404")) {
                TOT("创建订单失败");
            } else if (code.equals("200")) {
                Log.e("KTY", response);
                TOT("创建订单成功");
                //bean.getData().getHf_money()
                payChoosePopupwindow(bean.getData().getOrders_num(), "0.01", "购买会员");
            }
        }
    };
    //支付宝支付
    private StringCallback alipayCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            PayRequestBean bean = Http.model(PayRequestBean.class, response);
            if (bean.getCode().equals("200")) {
                payInerface(bean.getData());
            }
        }
    };

    private void payChoosePopupwindow(final String orderNum, final String price, final String title) {
        View popupWindow_view = getLayoutInflater().inflate(R.layout.item_popupwindow_pay, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(tiTitle, Gravity.BOTTOM, 0, 0);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        LinearLayout llWeixin = (LinearLayout) popupWindow_view.findViewById(R.id.ll_weixin);
        LinearLayout llAlipay = (LinearLayout) popupWindow_view.findViewById(R.id.ll_alipay);
        LinearLayout llUppay = (LinearLayout) popupWindow_view.findViewById(R.id.ll_uppay);
        ImageView ivQuit = (ImageView) popupWindow_view.findViewById(R.id.iv_quit);
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
                UserInfoModel.pay(alipayCallBack, orderNum, price, title);
            }
        });
        llUppay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                UserInfoModel.upPay(upPayCallBack, orderNum, price);
            }
        });
        ivQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }

    /**
     * 支付宝支付
     */
    private void payInerface(final String orderInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyVipActivity.this);
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
    /**
     * 银联支付
     */
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
    private String mMode = "00";//设置测试模式:01为测试 00为正式环境

    private void payUpPay(String tn) {
        UPPayAssistEx.startPay(this, null, null, tn, mMode);

    }

    StringCallback upPayCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("请求失败");
        }

        @Override
        public void onResponse(String response, int id) {
            UpPayRequestBean bean = Http.model(UpPayRequestBean.class, response);
            if (bean.getCode().equals("200")) {
                payUpPay(bean.getData().getTn());
            }
        }
    };
}
