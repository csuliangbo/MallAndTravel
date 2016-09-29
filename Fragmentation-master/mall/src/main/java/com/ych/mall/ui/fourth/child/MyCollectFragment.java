package com.ych.mall.ui.fourth.child;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.CollectBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.first.child.GoodsViewPagerFragment;
import com.ych.mall.ui.first.child.childpager.GoodsFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/14.
 */
@EFragment(R.layout.fragment_foot)
public class MyCollectFragment extends BaseFragment implements RecyclerViewModel.RModelListener<CollectBean.CollectData> {
    public static MyCollectFragment newInstance() {
        Bundle bundle = new Bundle();
        MyCollectFragment fragment = new MyCollectFragment_();
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
    @Click
    void tiText() {
        AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("确定要清收藏？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                UserInfoModel.clearCollect(clearCallBack);
            }
        });
        builder.show();
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
        final String id=t.getId();
        holder.setText(R.id.name, t.getDetail_title());
        holder.setText(R.id.price, t.getPrice_new());
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        holder.getView(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoModel.delCollect(delCallBack,id);
            }
        });
        final String gId=t.getGoods_id();
        final String type=t.getIs_type();
        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //商品
                if (type.equals("1")){
                    start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_GOODS, gId));
                }
                //旅游
                else{
                    start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_TRAVEL, gId));
                }

            }
        });
    }


    StringCallback delCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                model.refresh();
            } else
                TOT(bean.getMessage());

        }
    };

    StringCallback clearCallBack=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                model.reset();
            } else
                TOT(bean.getMessage());
        }
    };

}
