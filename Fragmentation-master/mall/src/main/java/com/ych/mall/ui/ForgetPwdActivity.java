package com.ych.mall.ui;

import android.os.Handler;
import android.os.Message;
import android.speech.tts.Voice;
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
@EActivity(R.layout.activity_forget_pwd)
public class ForgetPwdActivity extends BaseActivity {
    @ViewById
    EditText mPhone, mCode, mPwd;
    @ViewById
    TextView mGetCode;
    @ViewById
    ProgressButton onSubmit;

    @AfterViews
    void init() {
        onSubmit.setText("确定", "加载中...");
    }

    @Click
    void onSubmit() {
        if (isEmp(mPhone, "请输入手机号码"))
            return;
        if (isEmp(mCode, "请输入验证码"))
            return;
        if (isEmp(mPwd, "请输入密码"))
            return;
        onSubmit.startLoading();
        LoginAndRegistModel.rest(getT(mPhone), getT(mPwd), getT(mCode), callback);
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            onSubmit.stop();
            TOT("网络错误");
        }

        @Override
        public void onResponse(String response, int id) {
            onSubmit.stop();
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                TOT("修改成功");
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
