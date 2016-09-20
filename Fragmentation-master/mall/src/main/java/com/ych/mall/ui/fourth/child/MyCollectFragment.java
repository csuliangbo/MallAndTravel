package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.CollectBean;
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
public class MyCollectFragment extends BaseFragment implements RecyclerViewModel.RModelListener<CollectBean.CollectData>{
    public static MyCollectFragment newInstance(){
        Bundle bundle=new Bundle();
        MyCollectFragment fragment=new MyCollectFragment_();
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

    RecyclerViewNormalModel<CollectBean.CollectData> model;

    @AfterViews
    void init() {
        tiText.setVisibility(View.VISIBLE);
        tiText.setText("清除");
        tiTitle.setText("我的收藏");
        model = new RecyclerViewNormalModel<>(getActivity(), this, mRecyclerView, R.layout.item_foot);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        model.init(manager);
    }

    @Override
    public void getData(StringCallback callback, int page) {
        UserInfoModel.userCollect(callback);
    }

    @Override
    public void onErr(int state) {
        mLoading.setVisibility(View.GONE);
        TOT("网络链接错误");
    }

    @Override
    public List<CollectBean.CollectData> getList(String str) {
        mLoading.setVisibility(View.GONE);
        CollectBean bean = Http.model(CollectBean.class, str);
        if (bean.getCode().equals("200"))
            return bean.getData();
        else

            return null;
    }

    @Override
    public void covert(YViewHolder holder, CollectBean.CollectData t) {
        holder.setText(R.id.name, t.getDetail_title());
        holder.setText(R.id.price, t.getPrice_new());
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL+t.getPic_url());
    }
}
