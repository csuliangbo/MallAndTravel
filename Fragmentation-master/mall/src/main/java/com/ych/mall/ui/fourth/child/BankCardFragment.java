package com.ych.mall.ui.fourth.child;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.BankCardBean;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/11.
 */
@EFragment(R.layout.fragment_my_account_bank_card)
public class BankCardFragment extends BaseFragment implements RecyclerViewModel.RModelListener<BankCardBean.BankCardData> {
    public static BankCardFragment newInstance() {
        Bundle bundle = new Bundle();
        BankCardFragment fragment = new BankCardFragment_();
        fragment.setArguments(bundle);
        return fragment;

    }

    @ViewById
    TextView tiTitle;
    @ViewById
    ProgressButton onAdd;
    @ViewById
    RecyclerView mRecyclerView;

    @Click
    void onAdd() {
        start(NewBankCardFragment.newInstance());
    }

    RecyclerViewNormalModel model;
@Click
void onBack(){
    back();
}
    @AfterViews
    void init() {
        onAdd.setText("添加银行卡", "加载中...");
        model = new RecyclerViewNormalModel(getActivity(), this, mRecyclerView, R.layout.item_bank_card);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        model.init(manager);
        tiTitle.setText("我的银行卡");

    }

    @Override
    public void onResume() {
        super.onResume();
        model.refresh();
    }

    @Override
    public void getData(StringCallback callback, int page) {
        UserInfoModel.userBank(callback);
    }

    @Override
    public void onErr(int state) {
    }

    @Override
    public List<BankCardBean.BankCardData> getList(String str) {
        BankCardBean bean = Http.model(BankCardBean.class, str);
        if (bean.getCode().equals("200")) {
            List<BankCardBean.BankCardData> data = bean.getData();
            return data;
        }
        return null;
    }

    @Override
    public void covert(YViewHolder holder, BankCardBean.BankCardData t) {
        holder.setText(R.id.mBankName, t.getBelong());
        holder.setText(R.id.mType, t.getType());
        holder.setText(R.id.mNum, t.getCard_num());
        final String id = t.getId();
        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(id);
            }
        });
    }

    private void delete(final String id) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("解绑银行卡").setMessage("是否解绑此卡").setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onAdd.startLoading();
                        UserInfoModel.delBank(id, callback);
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接错误");
            onAdd.stop();
        }

        @Override
        public void onResponse(String response, int id) {
            onAdd.stop();
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200"))
                model.refresh();
            TOT(bean.getMessage());
        }
    };
}
