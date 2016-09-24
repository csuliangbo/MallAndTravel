package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.widget.ClearEditText;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/14.
 */
@EFragment(R.layout.fragment_new_bankcard)
public class NewBankCardFragment extends BaseFragment {
    public static NewBankCardFragment newInstance() {
        Bundle bundle = new Bundle();
        NewBankCardFragment fragment = new NewBankCardFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Click
    void onBack(){
        back();
    }
    @ViewById
    ClearEditText mName, mID, mType, mBankName, mCardNum, mPhone;
    @ViewById
    TextView tiTitle;
    @ViewById
    ProgressButton onSubmit;

    @AfterViews
    void init() {
        onSubmit.setText("添加", "提交中...");
        tiTitle.setText("添加银行卡");
    }

    @Click
    void onSubmit() {
        if (isEmp(mName, "请输入姓名"))
            return;
        if (isEmp(mID, "请输入身份证号"))
            return;
        if (isEmp(mType, "输入银行卡类型"))
            return;
        if (isEmp(mBankName, "输入银行卡名字"))
            return;
        if (isEmp(mCardNum, "输入银行卡号"))
            return;
        if (isEmp(mPhone, "输入绑定的电话号码"))
            return;
        onSubmit.startLoading();
        UserInfoModel.addBank(getT(mBankName), getT(mType), getT(mCardNum), getT(mName), getT(mID), getT(mPhone), callback);
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                back();
            }
            TOT(bean.getMessage());

        }
    };
}
