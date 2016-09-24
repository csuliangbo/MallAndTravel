package com.ych.mall.model;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.PayResult;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/24.
 */
public class PayModel {

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;
    private Activity mActivity;
    public static final int WEiXIN = 201;
    public static final int ZHIFUBAO = 202;
    private int currentType = ZHIFUBAO;

    public PayModel(Activity activity, String orderNum, String price, String title, int type) {
        mActivity = activity;
        UserInfoModel.pay(payCallBack, orderNum, price, title);
    }

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
                if (currentType == WEiXIN) {
                    payWeixin();
                } else {
                    payInerface(bean.getData());
                }

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
                PayTask alipay = new PayTask(mActivity);
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
    /**
     * 微信支付
     */
    private IWXAPI api;

    private void payWeixin() {
//        api = WXAPIFactory.createWXAPI(this,"wx5e85371c27606b8b");
//        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//        if (!isPaySupported) {
//            TOT("当前微信不支持支付功能");
//            return;
//        }
//        PayReq req = new PayReq();
//        req.appId = jsonObject.getString("appid");
//        req.partnerId = jsonObject.getString("partnerid");
//        req.prepayId = jsonObject.getString("prepayid");
//        req.nonceStr = jsonObject.getString("noncestr");
//        req.timeStamp = jsonObject.getString("timestamp");
//        req.packageValue = jsonObject.getString("package");
//        req.sign = jsonObject.getString("sign");
//        ViewInject.toast("正常调起支付");
//        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//        api.registerApp(MyApplication.APP_ID);
//        api.sendReq(req);
    }

    public void TOT(String str) {
        try {
            Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }
}
