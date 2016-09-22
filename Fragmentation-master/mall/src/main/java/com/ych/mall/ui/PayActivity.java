package com.ych.mall.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.ych.mall.R;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.PayResult;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

import java.util.Map;

/**
 * Created by ych on 2016/9/6.
 */
@EActivity(R.layout.activity_pay)
public class PayActivity extends BaseActivity {
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;

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
}
