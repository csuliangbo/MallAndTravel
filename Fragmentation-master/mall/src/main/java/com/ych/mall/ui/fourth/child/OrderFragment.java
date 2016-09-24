package com.ych.mall.ui.fourth.child;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ych.mall.R;
import com.ych.mall.bean.LogisticsBean;
import com.ych.mall.bean.OrderBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.PayActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

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
        mTab.addTab(mTab.newTab().setText("待评价"));


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
    public void covert(YViewHolder holder, OrderBean.OrderData t) {
        final String id = t.getOrders_num();
        holder.setText(R.id.id, "订单号:" + t.getOrders_num());
        int type = Integer.parseInt(t.getOrders_status());
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
                        startActivity(new Intent(getActivity(), PayActivity_.class));
                    }
                });
                break;
            case 1:
                typeText = "已支付";
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
                        UserInfoModel.getShop(getShopCallBack, id);
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
                        Log.e("KTY  @@", id);
                        start(LogisticsFragment.newInstance(id));
                    }
                });
                break;
            case 3:
                typeText = "已签收";
                break;
            case 4:
                typeText = "已申请退货";
                break;
            case 5:
                typeText = "已完成";
                btnLeft.setVisibility(View.VISIBLE);
                btnLeft.setText(complete);
                btnLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("掌中游商城提醒您")
                                .setMessage(titleHead + id + titleFoot)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
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
                });
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

    //取消
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
                model.onRefresh();
            }
            TOT(bean.getMessage());
        }
    };
}
