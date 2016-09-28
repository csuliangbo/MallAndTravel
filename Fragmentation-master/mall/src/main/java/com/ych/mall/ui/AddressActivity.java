package com.ych.mall.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.adapter.AddressAdapter;
import com.ych.mall.bean.AddressBean;
import com.ych.mall.bean.UserInfoBean;
import com.ych.mall.event.AddressEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.MeModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by yach on 2016/9/7.
 */
@EActivity(R.layout.activity_address)
public class AddressActivity extends BaseActivity implements RecyclerViewModel.RModelListener<UserInfoBean.UserInfoData> {
    @ViewById
    TextView tiTitle;
    @ViewById
    TextView tiText;
    @ViewById
    RecyclerView dataList;
    RecyclerViewNormalModel<UserInfoBean.UserInfoData> model;

    @Click
    void onBack() {
        finish();
    }

    boolean loadFromLast = false;
    @ViewById
    ProgressButton addNewAdd;

    @Click
    public void addNewAdd() {
        startActivity(new Intent(this, NewAddressActivity_.class));
    }

    @AfterViews
    public void initViews() {
        EventBus.getDefault().register(this);
        addNewAdd.setText("添加新地址", "加载中");
        addNewAdd.startLoading();
        tiTitle.setText("收货地址");
        tiText.setVisibility(View.GONE);
        model = new RecyclerViewNormalModel<>(this, this, dataList, R.layout.item_address);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        model.init(manager);


    }


    @Override
    public void getData(StringCallback callback, int page) {
        UserInfoModel.getUserInfo(UserCenter.getInstance().getCurrentUserId(), callback);
    }

    @Override
    public void onErr(int state) {
        TOT("网络链接失败");
        addNewAdd.stop();
    }

    @Override
    public List<UserInfoBean.UserInfoData> getList(String str) {
        addNewAdd.stop();

        UserInfoBean bean = Http.model(UserInfoBean.class, str);
        if (bean.getCode().equals("200")) {
            if (bean.getData().get(0).getProv() == null)
                return null;
            return bean.getData();
        } else {
            TOT(bean.getMessage());
            return null;

        }

    }

    @Override
    public void covert(YViewHolder holder, UserInfoBean.UserInfoData t) {
        final UserInfoBean.UserInfoData d = t;
        holder.setText(R.id.name, t.getAddressrealname() + "\t\t\t\t" + t.getAddressmobile());
        holder.setText(R.id.add, t.getProv() + t.getCity() + t.getDist() + t.getAddress());
        final String name = t.getAddressrealname();
        final String phone = t.getMobile();
        final String address = t.getProv() + t.getCity() + t.getDist() + t.getAddress();
        if (t.getStatus().equals("1"))
            holder.getView(R.id.defalut).setVisibility(View.VISIBLE);
        else
            holder.getView(R.id.defalut).setVisibility(View.GONE);
        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getBooleanExtra("address", false)) {
                    finish();
                    EventBus.getDefault().post(new AddressEvent(address, name, phone));
                }
            }
        });
        holder.getView(R.id.mEdit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressActivity.this, NewAddressActivity_.class).putExtra(KV.DATA, d));
            }
        });
    }

    @Subscribe
    public void onEvent(UserInfoBean.UserInfoData data) {
        if (data.getStatus().equals("1")) {
            loadFromLast = false;
            addNewAdd.startLoading();
            model.refresh();
        } else
            model.insertData(data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}

