package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.HttpModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.PatternUtil;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * 意见反馈
 */
@EFragment(R.layout.fragment_advise)
public class AdviseFragment extends BaseFragment {
    public static AdviseFragment newInstance() {
        Bundle bundle = new Bundle();
        AdviseFragment fragment = new AdviseFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    @ViewById(R.id.et_phone)
    EditText etPhone;
    @ViewById(R.id.et_advise)
    EditText etAdvise;

    @Click
    void onBack() {
        back();
    }

    @Click
    void onCommit() {
        String phone = etPhone.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            if (!PatternUtil.isMobileNO(phone)) {
                TOT("请输入正确的手机号码");
                return;
            }
        }
        UserInfoModel.userAdvise(adviseCallback, etAdvise.getText().toString(), etPhone.getText().toString());
    }

    @AfterViews
    void init() {
        tiTitle.setText("意见反馈");
    }

    StringCallback adviseCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals(201)) {
                TOT("留言内容为空");
            } else if (bean.getCode().equals("400")) {
                TOT("留言失败");
            } else if (bean.getCode().equals("200")) {
                TOT("留言成功");
                etAdvise.setText("");
            }

        }
    };

}
