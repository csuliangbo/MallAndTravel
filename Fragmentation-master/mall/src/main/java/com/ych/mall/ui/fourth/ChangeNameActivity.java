package com.ych.mall.ui.fourth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.KV;
import com.ych.mall.widget.ClearEditText;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/9.
 */
@EActivity(R.layout.activity_change_name)
public class ChangeNameActivity extends BaseActivity {
    @ViewById
    TextView tiTitle;
    @ViewById
    ClearEditText mEditText;
    @ViewById
    ClearEditText mNewPwd;
    @ViewById
    View mView1;
    @ViewById
    View mView2;
    @ViewById
    ClearEditText mNewPwd2;
    @ViewById
    TextView mRemark;
    final int TYPE_NAME = 0;
    final int TYPE_PWD = 1;
    final int TYPE_ID = -1;
    int type = TYPE_NAME;

    @Click
    void onBack() {
        finish();
    }

    @AfterViews
    void initView() {

        if (getIntent().getIntExtra(KV.TYPE, 0) == -1) {
            tiTitle.setText("修改身份证号");
            mRemark.setText("请填写正确的身份证");
            type = TYPE_ID;
        } else if (getIntent().getIntExtra(KV.TYPE, 0) == 1) {
            type = TYPE_PWD;
            tiTitle.setText("修改密码");
            mNewPwd.setVisibility(View.VISIBLE);
            mNewPwd2.setVisibility(View.VISIBLE);
            mView1.setVisibility(View.VISIBLE);
            mView2.setVisibility(View.VISIBLE);
            mRemark.setVisibility(View.GONE);
        } else
            tiTitle.setText("修改名字");
    }

    @Click
    void onSubmit() {
        if (mEditText.getText().toString() == null || mEditText.getText().toString().equals(""))
            return;
        switch (type) {
            case TYPE_ID:
                UserInfoModel.changeID(getT(mEditText), callback);
                break;
            case TYPE_NAME:
                UserInfoModel.changeName(getT(mEditText), callback);
                break;
            case TYPE_PWD:
                if (!getT(mNewPwd).trim().equals(getT(mNewPwd2).trim()))
                    return;
                UserInfoModel.changePwd(callback, getT(mEditText), getT(mNewPwd));
                break;
        }

    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            TOT(bean.getMessage());
            if (bean.getCode().equals("200")) {
                if (type != TYPE_PWD)
                    setResult(RESULT_OK, new Intent().putExtra(KV.DATA, mEditText.getText().toString()));
                finish();
            }

        }
    };

}
