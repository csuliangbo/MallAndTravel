package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.MyFootBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by ych on 2016/9/14.
 */
@EFragment(R.layout.fragment_foot)
public class MyFootFragment extends BaseFragment implements RecyclerViewModel.RModelListener<MyFootBean.MyFootData> {

    public static MyFootFragment newInstance() {
        Bundle bundle = new Bundle();
        MyFootFragment fragment = new MyFootFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById
    TextView tiTitle, tiText;
    @ViewById
    RecyclerView mRecyclerView;
    @ViewById
    TextView mLoading;

    @Click
    void onBack() {
        back();
    }

    RecyclerViewNormalModel<MyFootBean.MyFootData> model;

    @AfterViews
    void init() {
        tiText.setVisibility(View.VISIBLE);
        tiText.setText("清除");
        tiTitle.setText("我的足迹");
        model = new RecyclerViewNormalModel<>(getActivity(), this, mRecyclerView, R.layout.item_foot);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        model.init(manager);
    }

    @Override
    public void getData(StringCallback callback, int page) {
        UserInfoModel.userFoot(callback);
    }

    @Override
    public void onErr(int state) {
        mLoading.setVisibility(View.GONE);
        TOT("网络链接错误");
    }

    @Override
    public List<MyFootBean.MyFootData> getList(String str) {
        if (mLoading!=null)
        mLoading.setVisibility(View.GONE);
        MyFootBean bean = Http.model(MyFootBean.class, str);
        if (bean.getCode().equals("200"))
            return bean.getData();
        else

            return null;
    }

    @Override
    public void covert(YViewHolder holder, MyFootBean.MyFootData t) {
        holder.setText(R.id.name, t.getGoods_name());
        holder.setText(R.id.price, t.getGoods_price());
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL+t.getGoods_pic());
    }
}
