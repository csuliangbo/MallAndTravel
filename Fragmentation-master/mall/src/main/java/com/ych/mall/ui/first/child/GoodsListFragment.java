package com.ych.mall.ui.first.child;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.GoodsListBean;
import com.ych.mall.bean.GoodsListBean.GoodsListData;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.first.child.childpager.GoodsFragment;
import com.ych.mall.ui.first.child.childpager.SearchFragment;
import com.ych.mall.utils.KV;
import com.ych.mall.widget.ClearEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ych on 2016/9/5.
 */
@EFragment(R.layout.fragment_good_list)
public class GoodsListFragment extends BaseFragment implements RecyclerViewModel.RModelListener<GoodsListData> {
    @ViewById(R.id.swipe)
    SwipeRefreshLayout layout;
    @ViewById(R.id.recycle)
    RecyclerView rv;
    @ViewById
    TextView mLoading;
    @ViewById(R.id.onSearch)
    ClearEditText onSearch;

    @Click
    void tvSearch() {
        start(SearchFragment.newInstance(onSearch.getText().toString(), GoodsFragment.TYPE_GOODS));
        hideSoftKeyBord();
    }

    @Click
    void onBack() {
        back();
    }

    String id;

    public static GoodsListFragment newInstance(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.ID, id);
        GoodsListFragment fragment = new GoodsListFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @AfterViews
    public void initViews() {
        id = getArguments().getString(KV.ID);
        RecyclerViewModel<GoodsListData> rvm =
                new RecyclerViewModel<GoodsListData>(getActivity(),
                        this,
                        rv,
                        layout,
                        R.layout.item_goods_list);
        rvm.setMiniSize(3);
        rvm.init();

    }

    //获取数据
    @Override
    public void getData(StringCallback callback, int page) {
        MallAndTravelModel.goodsList(callback, page, id);
    }

    //解析JSON数据
    @Override
    public List<GoodsListData> getList(String str) {
        mLoading.setVisibility(View.GONE);
        GoodsListBean bean = Http.model(GoodsListBean.class, str);
        if (bean.getCode().equals("200"))
            return bean.getData();
        return null;
    }

    //绑定item
    @Override
    public void covert(YViewHolder holder, GoodsListData t) {
        final String id = t.getId();
        holder.setText(R.id.name, t.getTitle());
        holder.setText(R.id.price, "￥" + t.getPrice_new());
        holder.setText(R.id.num, t.getFanli_jifen());
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());

        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_GOODS, id));
            }
        });
    }

    @Override
    public void onErr(int state) {
        TOT("网络获取失败");
        mLoading.setVisibility(View.GONE);
    }

}
