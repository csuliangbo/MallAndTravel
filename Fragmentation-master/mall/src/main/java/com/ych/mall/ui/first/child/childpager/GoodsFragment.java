package com.ych.mall.ui.first.child.childpager;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fyales.tagcloud.library.TagBaseAdapter;
import com.fyales.tagcloud.library.TagCloudLayout;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.ych.mall.R;

import com.ych.mall.bean.GoodsDetailBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.TravelDetailBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.ui.PayActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.fourth.WebViewActivity_;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.UserCenter;
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
    FrameLayout protocol;
    @ViewById(R.id.fg_stock)
    TextView stock;
    @ViewById(R.id.fg_time)
    TextView time;
    @ViewById(R.id.fg_order)
    Button order;
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
    @ViewById
    CheckBox mProtocol;
    @ViewById
    FrameLayout mLoading;
    @ViewById(R.id.ll_people_number)
    LinearLayout llPeopleNumber;
    @ViewById(R.id.tv_num_adult)
    TextView tvNumAdult;
    @ViewById(R.id.tv_num_children)
    TextView tvNumChildren;
    private int numAdult;
    private int numChildren;

    @Click
    void onProtocol() {
        web("http://www.zzumall.com/index.php/Mobile/Tourism/lvyou_xieyi");
    }

    List<GoodsDetailBean.Taocan> datas;
    List<String> mDate;
    TagBaseAdapter tAdapter;
    String groupId;
    String groupTitle;
    String mId;
    String mPrice;
    String mPoint;
    String goodsID;
    String goodsUrl = "http://www.zzumall.com/index.php/Mobile/Goods/goods_detail_m.html?id=";
    String travelUrl = "http://www.zzumall.com/index.php/Mobile/Tourism/tourism_detail_m.html?id=";
    String protocolUrl = "http://www.zzumall.com/index.php/Mobile/Tourism/lvyou_xieyi";

    @Click
    void tv_sub_num_adult() {
        numAdult--;
        if (numAdult <= 0)
            numAdult = 0;
        tvNumAdult.setText(numAdult + "");
    }

    @Click
    void tv_add_num_adult() {
        numAdult++;
        tvNumAdult.setText(numAdult + "");
    }

    @Click
    void tv_sub_num_children() {
        numChildren--;
        if (numChildren <= 0)
            numChildren = 0;
        tvNumChildren.setText(numChildren + "");
    }

    @Click
    void tv_add_num_children() {
        numChildren++;
        tvNumChildren.setText(numChildren + "");
    }

    //商品购买
    @Click
    void onBuy() {
        if (datas != null && datas.size() > 0) {
            if (groupTitle == null) {
                TOT("请选择套餐");
                return;
            }
        }
        startActivity(new Intent(getActivity(), PayActivity_.class).putExtra(KV.GOODS_ID, mId));
    }

    //旅游预订
    @Click
    public void fg_order() {
        if (UserCenter.getInstance().isTourist())
            return;
        if (groupTitle == null) {
            TOT("请选择套餐");
            return;
        }
        if (mProtocol.isChecked() == false) {
            TOT("请同意旅游协议");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(KV.GOODS_ID, mId);
        bundle.putInt("TYPE", TYPE_TRAVEL);
        bundle.putString("Date", groupTitle);
        startActivity(new Intent(getActivity(), PayActivity_.class).putExtras(bundle));

    }

    //分享
    @Click
    void ivShare() {
        umShare();
    }

    UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            TOT("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            TOT("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            TOT("分享取消了");
        }
    };

    private void umShare() {
        String url;
        if (currentType == TYPE_GOODS)
            url = goodsUrl + mId;
        else
            url = travelUrl + mId;
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
                {
                        SHARE_MEDIA.WEIXIN,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE
                };
        new ShareAction(getActivity()).setDisplayList(displaylist)
                .withText(mTitle.getText().toString())
                .withTitle("掌中游")
                .withTargetUrl(url)
                .setListenerList(umShareListener)
                .open();
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
            MallAndTravelModel.travelDetail(travelCallBack, mId);
        }
        if (UserCenter.getInstance().isTourist()) {
            order.setBackgroundColor(getActivity().getResources().getColor(R.color.text_gray));
            order.setText("请晋升为会员，继续购物");
            if (currentType == TYPE_GOODS) {
                order.setVisibility(View.VISIBLE);
                bottomLL.setVisibility(View.GONE);
            }
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
        llPeopleNumber.setVisibility(View.GONE);
    }

    @Click
    void onShopCar() {
        if (datas != null && datas.size() > 0) {
            if (groupTitle == null) {
                TOT("请选择套餐");
                return;
            }
        }
        MallAndTravelModel.addShopCar(shopCallBack, mId, groupTitle, mPoint, mPrice);
    }


    private void goods(GoodsDetailBean.GoodsDetailData t) {
        if (t == null)
            return;
        sT(mTitle, t.getTitle());
        sT(mPriceNew, "￥" + t.getPrice_new());
        sT(mPriceOld, t.getPrice_old());
        points.setText("送积分（" + t.getFanli_jifen() + "积分）");
        stock.setText("库存：" + t.getKucun() + "件");
        mPrice = t.getPrice_new();
        mPoint = t.getFanli_jifen();
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

    private void travel(TravelDetailBean.TravelDetaiData t) {
        if (t == null)
            return;
        mPriceOld.setVisibility(View.GONE);
        sT(mTitle, t.getTitle());
        sT(mPriceNew, t.getPrice_new());
        sT(mPriceOld, t.getPrice_old());
        points.setText("送积分（" + t.getFanli_jifen() + "积分）");
        //stock.setText("库存：" + t.getKucun() + "件");
        mPrice = t.getPrice_new();
        mPoint = t.getFanli_jifen();
        if (t.getPic_url() != null) {
            String[] banner = new String[t.getPic_tuji().size()];
            int c = 0;
            for (String s : t.getPic_tuji()) {
                banner[c] = Http.GOODS_PIC_URL + t.getPic_tuji().get(c);
                c++;
            }
            showView.setData(banner);
        }

        sT(city, "出发城市：" + t.getChufa_address());
        time.setVisibility(View.GONE);
        mDate = t.getChufa_date();
        if (mDate == null)
            return;
        packagell.setVisibility(View.VISIBLE);
        mGroup.setVisibility(View.GONE);
        List<String> tagDatas = new ArrayList<>();
        for (String s : mDate) {
            tagDatas.add(s);
        }
        tAdapter = new TagBaseAdapter(getActivity(), tagDatas);
        mTags.setAdapter(tAdapter);
        mTags.setItemClickListener(new TagCloudLayout.TagItemClickListener() {
            @Override
            public void itemClick(int position) {
                groupTitle = mDate.get(position);
            }
        });
    }

    void web(String url) {
        startActivity(new Intent(getActivity(), WebViewActivity_.class).putExtra(KV.URL, url));
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
            if (mLoading!=null)
            mLoading.setVisibility(View.GONE);

            try {
                GoodsDetailBean bean = Http.model(GoodsDetailBean.class, response);
                if (bean.getCode().equals("200")) {
                    goods(bean.getData().get(0));
                } else
                    TOT(bean.getMessage());
            } catch (Exception e) {

            }

        }
    };
    //旅游
    StringCallback travelCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public void onResponse(String response, int id) {
            mLoading.setVisibility(View.GONE);
            TravelDetailBean bean = Http.model(TravelDetailBean.class, response);
            if (bean.getCode().equals("200"))
                travel(bean.getData().get(0));
            else
                TOT(bean.getMessage());
        }
    };
    //添加购物车
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
}
