package com.ych.mall.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.LoginAndRegistModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/12.
 */
@EActivity(R.layout.activity_regist)
public class RegistActivity extends BaseActivity {
    @ViewById
    EditText mPhone, mCode, mPwd, mName, mID, mInvitation;
    @ViewById
    TextView mGetCode;
    @ViewById
    ProgressButton onSubmit;
    @ViewById(R.id.tiTitle)
    TextView tvTitle;

    @Click
    void onBack() {
        finish();
    }

    @AfterViews
    void init() {
        tvTitle.setText("注册");
        onSubmit.setText("注册", "注册中...");
        tiTitle.setText("注册");
    }

    @Click
    void onSubmit() {
        if (isEmp(mPhone, "手机号不能为空"))
            return;
        if (isEmp(mCode, "验证码不能为空"))
            return;
        if (isEmp(mPwd, "密码不能为空"))
            return;
        if (isEmp(mName, "姓名不能为空"))
            return;
        if (isEmp(mInvitation, "推荐人不能为空"))
            return;
        onSubmit.startLoading();
        LoginAndRegistModel.regist(getT(mPhone), getT(mCode),
                getT(mID), getT(mName), getT(mPwd),
                getT(mInvitation), callback);
    }

    @Click
    void onBack() {
        finish();
    }

    @ViewById
    TextView tiTitle;
    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            onSubmit.stop();
            TOT("注册失败，请重试");
        }

        @Override
        public void onResponse(String response, int id) {
            onSubmit.stop();
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                TOT("注册成功");
                finish();
            } else
                TOT(bean.getMessage());
        }
    };

    @Click
    void mGetCode() {
        if (isEmp(mPhone, "手机号不能为空"))
            return;
        if (mGetCode.getText().equals("获取验证码"))
            handler.sendEmptyMessageDelayed(0x123, 1000);
        LoginAndRegistModel.code(getT(mPhone));
    }

    int time = 60;
    boolean isOver = false;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (isOver)
                return;
            if (time == 1) {
                mGetCode.setText("获取验证码");
                time = 60;
                return;
            }
            time--;
            mGetCode.setText(time + "秒后重试");
            handler.sendEmptyMessageDelayed(0x123, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isOver = true;
    }


}
