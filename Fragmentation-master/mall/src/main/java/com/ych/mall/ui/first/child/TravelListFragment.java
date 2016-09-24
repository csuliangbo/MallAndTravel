package com.ych.mall.ui.first.child;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.GoodsListBean;
import com.ych.mall.bean.TravelListBean;
import com.ych.mall.bean.TravelListBean.TravelListData;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.first.child.childpager.GoodsFragment;
import com.ych.mall.utils.KV;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ych on 2016/9/6.
 */
@EFragment(R.layout.fragment_good_list)
public class TravelListFragment extends BaseFragment implements RecyclerViewModel.RModelListener<TravelListData> {
    @ViewById(R.id.swipe)
    SwipeRefreshLayout layout;
    @ViewById(R.id.recycle)
    RecyclerView rv;
    String id;
    @ViewById
    TextView mLoading;
    int type;

    public static TravelListFragment newInstance(String id, int type) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.ID, id);
        bundle.putInt(KV.TYPE, type);
        TravelListFragment fragment = new TravelListFragment_();
        fragment.setArguments(bundle);
        return fragment;

    }

    @AfterViews
    public void initViews() {
        setTAG("travel");
        type=getArguments().getInt(KV.TYPE);
        id = getArguments().getString(KV.ID);
        RecyclerViewModel<GoodsListBean> rvm =
                new RecyclerViewModel<GoodsListBean>(getActivity(),
                        this,
                        rv,
                        layout,
                        R.layout.item_travel_list);
        rvm.setMiniSize(3);
        rvm.init();

    }

    @Override
    public void getData(StringCallback callback, int page) {
        MallAndTravelModel.travelList(callback, page, id, type);
    }

    @Override
    public void onErr(int state) {
        TOT("网络链接失败");
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public List<TravelListData> getList(String str) {
        log(str);
        mLoading.setVisibility(View.GONE);
        TravelListBean bean = Http.model(TravelListBean.class, str);
        if (bean.getCode().equals("200"))
            return bean.getData();
        return null;
    }

    @Override
    public void covert(YViewHolder holder, TravelListData t) {
        final String id = t.getId();
        holder.setText(R.id.name, t.getTitle());
        holder.setText(R.id.price, "￥" + t.getPrice_new());
        holder.setText(R.id.num, "返利积分：" + t.getFanli_jifen());
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());

        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_TRAVEL, id));
            }
        });

    }
}
