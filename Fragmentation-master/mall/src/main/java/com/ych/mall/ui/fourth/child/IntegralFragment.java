package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.AccountBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/11.
 */
@EFragment(R.layout.fragment_my_account_score)
public class IntegralFragment extends BaseFragment {

    public static IntegralFragment newInstance() {
        Bundle bundle = new Bundle();
        IntegralFragment fragment = new IntegralFragment_();
        fragment.setArguments(bundle);
        return fragment;

    }

    @ViewById(R.id.tv_A)
    TextView tvA;
    @ViewById(R.id.tv_B)
    TextView tvB;

    @AfterViews
    void init() {
        UserInfoModel.accountManage(accountCallback);
    }

    StringCallback accountCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接错误");
        }

        @Override
        public void onResponse(String response, int id) {
            AccountBean bean = Http.model(AccountBean.class, response);
            if (bean.getCode().equals("200")) {
                tvA.setText(bean.getData().getAdd_jf_limit());
                tvB.setText(bean.getData().getAdd_jf_currency());
            }
        }
    };
}
