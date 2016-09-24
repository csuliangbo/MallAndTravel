package com.ych.mall.ui.fourth;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.LoginAndRegistModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.widget.ClearEditText;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/9.
 */
@EActivity(R.layout.activity_change_phone)
public class ChangePhoneActivity extends BaseActivity {

    @ViewById
    ClearEditText mCode,mPhone;
    @ViewById
    TextView mGetCode;
    @ViewById
    ProgressButton onSubmit;
    @Click
    void onSubmit(){
        if (isEmp(mCode,"输入验证码"))
            return;
        if (isEmp(mPhone,"输入手机号码"))
            return;
        onSubmit.startLoading();

    }
    @Click
    void mGetCode(){
        if (mGetCode.getText().equals("获取验证码"))
        handler.sendEmptyMessageDelayed(0x123, 1000);
        LoginAndRegistModel.code(UserCenter.getInstance().getCurrentUserPhone());
    }
    @AfterViews
    void init() {
        onSubmit.setText("修改", "修改中...");
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
    StringCallback callback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
            onSubmit.stop();
        }

        @Override
        public void onResponse(String response, int id) {
            onSubmit.stop();
            ParentBean bean= Http.model(ParentBean.class,response);
            if (bean.getCode().equals("200"))
            {
                UserCenter.getInstance().setCurrentUserPhone(getT(mPhone));
                finish();
            }else
                TOT(bean.getMessage());

        }
    };
}
