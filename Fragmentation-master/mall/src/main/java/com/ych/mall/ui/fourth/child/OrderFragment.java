package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ych.mall.R;
import com.ych.mall.bean.OrderBean;
import com.ych.mall.model.Http;
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

import java.util.List;

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
        mLoading.setVisibility(View.GONE);
        OrderBean bean = Http.model(OrderBean.class, str);
        if (bean.getCode().equals("200")) {
            return bean.getData();
        }
        return null;
    }

    @Override
    public void covert(YViewHolder holder, OrderBean.OrderData t) {
        holder.setText(R.id.id, "订单号:" + t.getOrders_num());
        int type = Integer.parseInt(t.getOrders_status());
        String typeText = null;
        switch (type) {
            case 0:
                typeText = "订单默认状态";
                break;
            case 1:
                typeText = "已支付";
                break;
            case 2:
                typeText = "已发货";
                break;
            case 3:
                typeText = "已签收";
                break;
            case 4:
                typeText = "已申请退货";
                break;
            case 5:
                typeText = "已完成";
                break;
        }
        String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").
                format(new java.util.Date(Long.parseLong(t.getCreate_time()) * 1000));
        holder.setText(R.id.status, typeText);
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        holder.setText(R.id.price, "￥" + t.getPrice_new());
        holder.setText(R.id.num, "x" + t.getGoods_num());
        holder.setText(R.id.priceAll, t.getPrice_sum());
        holder.setText(R.id.time, "下单时间:" + date);
        holder.setText(R.id.name, t.getGoods_title());
    }
}
