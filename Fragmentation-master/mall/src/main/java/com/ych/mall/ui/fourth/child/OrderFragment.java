package com.ych.mall.ui.fourth.child;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;
import com.ych.mall.MyApp;
import com.ych.mall.R;
import com.ych.mall.bean.AuthResult;
import com.ych.mall.bean.OrderBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.PayRequestBean;
import com.ych.mall.bean.PayResult;
import com.ych.mall.bean.UpPayRequestBean;
import com.ych.mall.bean.WeixinPayBean;
import com.ych.mall.event.AddressEvent;
import com.ych.mall.event.RefundEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.HttpModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.Tools;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.wxapi.WXPayEntryActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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
    public final static int TYPE_PAY = 11;
    public final static int TYPE_WAIT = 12;
    public final static int TYPE_COMMENT = 13;
    public final static int TYPE_ALL = 14;
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

    RecyclerViewModel<OrderBean.Goods> itemModel;
    SwipeRefreshLayout itemSwipeRefreshLayout;

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
        setTAG("order");
        EventBus.getDefault().register(this);
        currentType = getArguments().getInt(KV.TYPE, TYPE_ALL);
        tiTitle.setText("我的订单");
        mTab.addTab(mTab.newTab().setText("全部"));
        mTab.addTab(mTab.newTab().setText("待付款"));
        mTab.addTab(mTab.newTab().setText("待收货"));
        //mTab.addTab(mTab.newTab().setText("待评价"));

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
                if (!model.isEmpty())
                    model.onRefresh();
                else
                    model.init();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(RefundEvent e) {
        if (e.isRefund()) {
            model.onRefresh();
        }
    }

    @Override
    public void getData(StringCallback callback, int page) {
        log(currentType);
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
        log(str);
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
    public void covert(YViewHolder holder, final OrderBean.OrderData t) {
        if (t.getGoods() != null) {
            RecyclerView recyclerView = holder.getView(R.id.order_item);
            itemModel = new RecyclerViewModel<>(getActivity(), new ItemRecycler(t.getGoods()), recyclerView, itemSwipeRefreshLayout, R.layout.item_order_recyclerview);
            itemModel.init();
        }
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
                if (t.getPay_status().equals("0")) {
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
                            payChoosePopupwindow(t.getOrders_num(), t.getPrice_shiji(), "shangpin");

                        }
                    });
                } else if (t.getPay_status().equals("1")) {
                    typeText = "待发货";
                    btnLeft.setVisibility(View.GONE);
                    btnMiddle.setVisibility(View.VISIBLE);
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
                            inputPassword(id);
                        }
                    });
                    btnMiddle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            start(SalesReturn.newInstance(id));
                        }
                    });
                    btnRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            start(LogisticsFragment.newInstance(id));
                        }
                    });
                }
                break;
            case 2:
                typeText = "已发货";
                btnLeft.setVisibility(View.VISIBLE);
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
                        inputPassword(id);
                    }
                });
                btnMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(SalesReturn.newInstance(id));
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
                        inputPassword(id);
                    }
                });
                btnMiddle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        start(SalesReturn.newInstance(id));
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
        if (t.getPrice_shiji() == null || t.getPrice_shiji().equals("0")) {
            double price = 0;
            if (t.getGoods() != null) {
                for (int i = 0; i < t.getGoods().size(); i++) {
                    if (t.getGoods().get(i).getPrice_new() != null)
                        price = Tools.add(price, Double.parseDouble(t.getGoods().get(i).getPrice_new()));
                }
            }
            holder.setText(R.id.priceAll, price + "");
        } else {
            holder.setText(R.id.priceAll, t.getPrice_shiji());
        }

    }

    //取消订单
    StringCallback cancelCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
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
        View popupWindow_view = getActivity().getLayoutInflater().inflate(R.layout.item_popupwindow_pay, null, false);
        final PopupWindow popupWindow = new PopupWindow(popupWindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0);
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
                UserInfoModel.weixinPay(weixinCallBack, orderNum, price);
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
     * 银联支付
     */
    private static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
    private String mMode = "00";//设置测试模式:01为测试 00为正式环境

    private void payUpPay(String tn) {
        UPPayAssistEx.startPay(getActivity(), null, null, tn, mMode);

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

    private void payWeixin(WeixinPayBean.WeixinPayData data) {
        api = WXAPIFactory.createWXAPI(getActivity(), data.getAppid(), false);
        //"wxc60cf9d8efdbbd50"
        api.registerApp(data.getAppid());
        PayReq req = new PayReq();
        req.appId = data.getAppid();
        req.partnerId = data.getPartnerid();
        req.prepayId = data.getPrepayid();
        req.packageValue = "Sign=WXPay";
        req.nonceStr = data.getNoncestr();
        req.sign = data.getSign();
        req.timeStamp = data.getTimestamp();
        TOT("正在调起支付");
        api.sendReq(req);
        MyApp.isPayActivity = false;

    }

    //微信支付
    private StringCallback weixinCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            WeixinPayBean bean = Http.model(WeixinPayBean.class, response);
            if (bean.getCode().equals("200")) {
                payWeixin(bean.getData());
            }
        }
    };

    //item的class
    private class ItemRecycler implements RecyclerViewModel.RModelListener<OrderBean.Goods> {
        private List<OrderBean.Goods> goodsList;

        public ItemRecycler(List<OrderBean.Goods> goodsList) {
            this.goodsList = goodsList;
        }

        @Override
        public void getData(StringCallback callback, int page) {
            callback.onResponse("", -1);
        }

        @Override
        public void onErr(int state) {

        }

        @Override
        public List<OrderBean.Goods> getList(String str) {
            return goodsList;
        }

        @Override
        public void covert(YViewHolder holder, OrderBean.Goods goods) {
            holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + goods.getPic_url());
            holder.setText(R.id.price, "￥" + goods.getPrice_new());
            holder.setText(R.id.num, "x" + goods.getGoods_num());
            holder.setText(R.id.name, goods.getGoods_title());
        }
    }

    private void inputPassword(final String id) {
        final EditText etInput = new EditText(getActivity());
        etInput.setTransformationMethod(PasswordTransformationMethod.getInstance()); //设置为密码输入框
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("请输入密码").setView(etInput)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = etInput.getText().toString();
                        if (!TextUtils.isEmpty(input)) {
                            confirmGetGood(id, input);
                        } else {
                            TOT("密码输入错误");
                        }

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    private void confirmGetGood(final String id, final String passWord) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("确认收货")
                .setMessage("确认收货之后，将无法退货，您确定吗")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserInfoModel.getShop(getShopCallBack, id, passWord);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }


}
