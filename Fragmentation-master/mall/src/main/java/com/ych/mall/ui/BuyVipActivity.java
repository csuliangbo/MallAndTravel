package com.ych.mall.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.ych.mall.R;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.CreateOrderBean;
import com.ych.mall.bean.CreateVipBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.PayBean;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.PayResult;
import com.ych.mall.model.Http;
import com.ych.mall.model.LoginAndRegistModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.UserCenter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

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
        canBuyVip();
    }

    @Click
    void onQuit(){
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
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            final ParentBean bean = Http.model(PayBean.class, response);
            String code = bean.getCode();
            if (code.equals("41")) {
                TOT("已经具有会员资格,无需购买");
            } else if (code.equals("201")) {
                LoginAndRegistModel.createVipOrder(buyVipCallback, 365 + "");
            } else if (code.equals("200")) {
                TOT("可以任意购买");
                final AlertDialog.Builder builder = new AlertDialog.Builder(
                        BuyVipActivity.this);
                builder.setTitle("请选择一下城市");
                builder.setSingleChoiceItems(new String[]{"会员", "合伙人"}, 1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    money = 365 + "";
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
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
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
                UserInfoModel.pay(payCallBack, bean.getData().getOrders_num(), bean.getData().getOrders_num(), "购买会员");
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
}
