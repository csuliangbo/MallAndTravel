package com.ych.mall.ui.first.child.childpager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fyales.tagcloud.library.TagBaseAdapter;
import com.fyales.tagcloud.library.TagCloudLayout;
import com.ych.mall.R;

import com.ych.mall.bean.GoodsDetailBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.ui.PayActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;
import com.ych.mall.widget.SlideShowView;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;


/**
 * Created by ych on 2016/9/5.
 */
@EFragment(R.layout.fragment_goods)
public class GoodsFragment extends BaseFragment {
    public static final int TYPE_GOODS = 11;
    public static final int TYPE_TRAVEL = 12;

    private int currentType = TYPE_TRAVEL;
    @ViewById(R.id.fg_img)
    SlideShowView showView;
    @ViewById(R.id.fg_bottom)
    LinearLayout bottomLL;
    @ViewById(R.id.fg_city)
    TextView city;
    @ViewById(R.id.fg_package)
    LinearLayout packagell;
    @ViewById(R.id.fg_points)
    TextView points;
    @ViewById(R.id.fg_protocol)
    CheckBox protocol;
    //库存
    @ViewById(R.id.fg_stock)
    TextView stock;
    @ViewById(R.id.fg_time)
    TextView time;
    @ViewById(R.id.fg_order)
    Button order;
    String mId;


    @ViewById
    TextView mTitle;
    @ViewById
    TextView mPriceNew;
    @ViewById
    TextView mPriceOld;
    @ViewById
    TagCloudLayout mTags;
    @ViewById
    TextView mGroup;
    List<GoodsDetailBean.Taocan> datas;
    TagBaseAdapter tAdapter;
    String groupId;
    String groupTitle;
    @ViewById
    TextView mLoading;
    String mPrice;
    String mPoint;

    @Click
    public void fg_order() {
        startActivity(new Intent(getActivity(), PayActivity_.class));

    }

    public static GoodsFragment newInstance(int type, String id) {
        Bundle bundle = new Bundle();
        bundle.putInt(KV.TYPE, type);
        bundle.putString(KV.ID, id);
        GoodsFragment fragment = new GoodsFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    public void initView() {
        currentType = getArguments().getInt(KV.TYPE, TYPE_TRAVEL);
        mId = getArguments().getString(KV.ID);
        if (currentType == TYPE_GOODS) {
            goodsInit();
            MallAndTravelModel.goodsDetail(goodsCallBack, mId);
        } else {

        }

    }

    private void goodsInit() {
        stock.setVisibility(View.VISIBLE);
        protocol.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        city.setVisibility(View.GONE);
        packagell.setVisibility(View.VISIBLE);
        bottomLL.setVisibility(View.VISIBLE);
        order.setVisibility(View.GONE);
    }

    private void goods(GoodsDetailBean.GoodsDetailData t) {
        if (t == null)
            return;

        sT(mTitle, t.getTitle());
        sT(mPriceNew, t.getPrice_new());
        sT(mPriceOld, t.getPrice_old());
        points.setText("送积分（" + t.getFanli_jifen() + "积分）");
        stock.setText("库存：" + t.getKucun() + "件");
        mPrice=t.getPrice_new();
        mPoint=t.getFanli_jifen();
        if (t.getPic_tuji() != null) {
            String[] banner = new String[t.getPic_tuji().size()];
            int c = 0;
            for (String s : t.getPic_tuji()) {
                banner[c] = Http.GOODS_PIC_URL + t.getPic_tuji().get(c);
                c++;
            }
            showView.setData(banner);
        }
        datas = t.getTaocan();
        if (datas == null || datas.size() < 1)
            return;
        mGroup.setVisibility(View.GONE);
        List<String> tagDatas = new ArrayList<>();
        for (GoodsDetailBean.Taocan ta : datas) {
            tagDatas.add(ta.getGuige_title());
        }
        tAdapter = new TagBaseAdapter(getActivity(), tagDatas);
        mTags.setAdapter(tAdapter);
        mTags.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                groupTitle = datas.get(position).getGuige_title();
            }
        });

    }

    @Click
    void onShopCar() {
        MallAndTravelModel.addShopCar(shopCallBack, mId, groupTitle,mPoint,mPrice);
    }

    StringCallback shopCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public void onResponse(String response, int id) {
            mLoading.setVisibility(View.GONE);
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
            }
            TOT(bean.getMessage());

        }
    };

    private void travel() {

    }

    //商品
    StringCallback goodsCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public void onResponse(String response, int id) {
            mLoading.setVisibility(View.GONE);
            GoodsDetailBean bean = Http.model(GoodsDetailBean.class, response);
            if (bean.getCode().equals("200")) {
                goods(bean.getData().get(0));
            } else
                TOT(bean.getMessage());

        }
    };
    //旅游
    StringCallback travelCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {

        }
    };
}
