package com.ych.mall.ui.fourth.child;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ych.mall.R;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.OrderBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.PayResult;
import com.ych.mall.model.Http;
import com.ych.mall.model.HttpModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/9.
 */
@EFragment(R.layout.fragment_order)
public class OrderFragment extends BaseFragment implements RecyclerViewModel.RModelListener<OrderBean.OrderData> {
    public final static int TYPE_PAY = 1;
    public final static int TYPE_WAIT = 2;
    public final static int TYPE_COMMENT = 3;
    public final static int TYPE_ALL = 4;
    int currentType = TYPE_ALL;
    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_AUTH_FLAG = 2;

    public static OrderFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(KV.TYPE, type);
        OrderFragment fragment = new OrderFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById
    TextView tiTitle;
    @ViewById
    TabLayout mTab;
    @ViewById
    TextView mLoading;
    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerViewModel<OrderBean.OrderData> model;

    private String titleHead = "点击后该订单(";
    private String titleFoot = ")所有商品不可退货，积分返利立即到账，是否同意?";
    boolean canSalesReturn = false;

    @Click
    void onBack() {
        back();
    }

    @AfterViews
    void initView() {
        //R.layout.item_order
        currentType = getArguments().getInt(KV.TYPE, TYPE_ALL);
        tiTitle.setText("我的订单");
        mTab.addTab(mTab.newTab().setText("全部"));
        mTab.addTab(mTab.newTab().setText("待付款"));
        mTab.addTab(mTab.newTab().setText("待收货"));
        //mTab.addTab(mTab.newTab().setText("待评价"));


        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("全部"))
                    currentType = TYPE_ALL;
                if (tab.getText().toString().equals("待付款"))
                    currentType = TYPE_PAY;
                if (tab.getText().toString().equals("待收货"))
                    currentType = TYPE_WAIT;
                if (tab.getText().toString().equals("待评价"))
                    currentType = TYPE_COMMENT;
                mLoading.setVisibility(View.VISIBLE);
                model.onRefresh();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        model = new RecyclerViewModel<>(getActivity(), this, mRecyclerView, mSwipeRefreshLayout, R.layout.item_order);
        model.init();
        switch (currentType) {
            case TYPE_ALL:
                mTab.getTabAt(0).select();

                break;
            case TYPE_PAY:
                mTab.getTabAt(1).select();
                break;
            case TYPE_WAIT:
                mTab.getTabAt(2).select();
                break;
            case TYPE_COMMENT:
                mTab.getTabAt(3).select();
                break;
        }
    }

    @Override
    public void getData(StringCallback callback, int page) {
        switch (currentType) {
            case TYPE_ALL:
                UserInfoModel.orderAll(callback, page);
                break;
            case TYPE_PAY:
                UserInfoModel.orderPay(callback, page);
                break;
            case TYPE_WAIT:
                UserInfoModel.orderAccept(callback, page);
                break;
            case TYPE_COMMENT:
                UserInfoModel.orderComment(callback, page);
                break;

        }
    }

    @Override
    public void onErr(int state) {
        TOT("网络链接失败");
    }

    @Override
    public List<OrderBean.OrderData> getList(String str) {
        if (mLoading != null)
            mLoading.setVisibility(View.GONE);
        OrderBean bean = Http.model(OrderBean.class, str);
        if (bean.getCode().equals("200")) {
            return bean.getData();
        }
        return null;
    }

    private String cancleOreder = "取消订单";
    private String nowPay = "立即付款";
    private String getShop = "确认收货";
    private String refund = "退货";
    private String logistics = "查看物流";
    private String complete = "标记完成";

    @Override
    public void covert(YViewHolder holder, final OrderBean.OrderData t)  {
        final String id = t.getOrders_num();
        holder.setText(R.id.id, "订单号:" + t.getOrders_num());
        final int type = Integer.parseInt(t.getOrders_status());
        String typeText = null;
        Button btnLeft = holder.getView(R.id.btnLeft);
        Button btnMiddle = holder.getView(R.id.btnMiddle);
        Button btnRight = holder.getView(R.id.btnRight);
        btnLeft.setVisibility(View.GONE);
        btnMiddle.setVisibility(View.GONE);
        btnRight.setVisibility(View.GONE);
        switch (type) {
            case 0:
                typeText = "待付款";
                btnLeft.setVisibility(View.VISIBLE);
                btnMiddle.setVisibility(View.VISIBLE);
                btnLeft.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnMiddle.setBackgroundResource(R.drawable.shape_green_dark_5dp);
                btnMiddle.setTextColor(getResources().getColor(R.color.white));
                btnLeft.setTextColor(getResources().getColor(R.color.gray2));
                btnLeft.setText(cancleOreder);
                btnMiddle.setText(nowPay);
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoModel.cancelOrder(cancelCallBack, id);
                    }
                });
                btnMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoModel.pay(payCallBack, t.getOrders_num(), 0.01 + "", t.getGoods_title());
                    }
                });
                break;
            case 1:
                typeText = "已支付";
                btnLeft.setVisibility(View.GONE);
                btnMiddle.setVisibility(View.GONE);
                btnRight.setVisibility(View.GONE);
                btnLeft.setBackgroundResource(R.drawable.shape_green_dark_5dp);
                btnMiddle.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnRight.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnMiddle.setTextColor(getResources().getColor(R.color.gray2));
                btnRight.setTextColor(getResources().getColor(R.color.gray2));
                btnLeft.setTextColor(getResources().getColor(R.color.white));
                btnLeft.setText(getShop);
                btnMiddle.setText(refund);
                btnRight.setText(logistics);
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoModel.getShop(getShopCallBack, id);
                    }
                });
                btnMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 3) {
                            start(SalesReturn.newInstance(id));
                        } else {
                            TOT("还未签收，不能退货");
                        }
                    }
                });
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(LogisticsFragment.newInstance(id));
                    }
                });
                break;
            case 2:
                typeText = "已发货";
                btnLeft.setVisibility(View.VISIBLE);
                btnMiddle.setVisibility(View.GONE);
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setBackgroundResource(R.drawable.shape_green_dark_5dp);
                btnMiddle.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnRight.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnMiddle.setTextColor(getResources().getColor(R.color.gray2));
                btnRight.setTextColor(getResources().getColor(R.color.gray2));
                btnLeft.setTextColor(getResources().getColor(R.color.white));
                btnLeft.setText(getShop);
                btnMiddle.setText(refund);
                btnRight.setText(logistics);
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoModel.getShop(getShopCallBack, id);
                    }
                });
                btnMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 3) {
                            start(SalesReturn.newInstance(id));
                        } else {
                            TOT("还未签收，不能退货");
                        }
                    }
                });
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(LogisticsFragment.newInstance(id));
                    }
                });
                break;
            case 3:
                typeText = "已签收";
                btnLeft.setVisibility(View.GONE);
                btnMiddle.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                btnLeft.setBackgroundResource(R.drawable.shape_green_dark_5dp);
                btnMiddle.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnRight.setBackgroundResource(R.drawable.shape_gray_dark_5dp);
                btnMiddle.setTextColor(getResources().getColor(R.color.gray2));
                btnRight.setTextColor(getResources().getColor(R.color.gray2));
                btnLeft.setTextColor(getResources().getColor(R.color.white));
                btnLeft.setText(getShop);
                btnMiddle.setText(refund);
                btnRight.setText(logistics);
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfoModel.getShop(getShopCallBack, id);
                    }
                });
                btnMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (type == 3) {
                            start(SalesReturn.newInstance(id));
                        } else {
                            TOT("还未签收，不能退货");
                        }
                    }
                });
                btnRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(LogisticsFragment.newInstance(id));
                    }
                });
                break;
            case 4:
                typeText = "已申请退货";
                break;
            case 5:
                typeText = "已完成";
//                btnLeft.setVisibility(View.VISIBLE);
//                btnLeft.setText(complete);
//                btnLeft.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("掌中游商城提醒您")
//                                .setMessage(titleHead + id + titleFoot)
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                }).create();
//                        dialog.show();
//                    }
//                });
                break;
        }
        if (t.getCreate_time() != null) {
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").
                    format(new java.util.Date(Long.parseLong(t.getCreate_time()) * 1000));
            holder.setText(R.id.time, "下单时间:" + date);
        }
        holder.setText(R.id.status, typeText);
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        holder.setText(R.id.price, "￥" + t.getPrice_new());
        holder.setText(R.id.num, "x" + t.getGoods_num());
        holder.setText(R.id.priceAll, t.getPrice_sum());
        holder.setText(R.id.name, t.getGoods_title());
    }

    //取消订单
    StringCallback cancelCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            Log.e("KTY@@@@", response);
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                model.onRefresh();
            }
            TOT(bean.getMessage());
        }
    };
    //确认收货
    StringCallback getShopCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                canSalesReturn = true;
                model.onRefresh();
            }
            TOT(bean.getMessage());
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
                payChoosePopupwindow(bean.getData());
            }
        }
    };

    private void payChoosePopupwindow(final String orderInfo) {

        View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindow_pay, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        popupWindow.setBackgroundDrawable(dw);
        LinearLayout llWeixin = (LinearLayout) popupWindow_view.findViewById(R.id.ll_weixin);
        LinearLayout llAlipay = (LinearLayout) popupWindow_view.findViewById(R.id.ll_alipay);
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
                payInerface(orderInfo);
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
                PayTask alipay = new PayTask(getActivity());
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
                        model.onRefresh();
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
        api = WXAPIFactory.createWXAPI(getActivity(), "wxb4ba3c02aa476ea1");
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
}
