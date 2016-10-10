package com.ych.mall.ui;

import android.app.Activity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.ych.mall.MainActivity;
import com.ych.mall.MainActivity_;
import com.ych.mall.R;
import com.ych.mall.bean.LoginBean;
import com.ych.mall.event.LoginEvent;
import com.ych.mall.event.MainEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.LoginAndRegistModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.ui.fourth.WebViewActivity_;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/12.
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {
    @ViewById
    EditText mPhone, mPwd;
    @ViewById
    ProgressButton onSubmit;

    @AfterViews
    void init() {
        onSubmit.setText("登录", "登录中...");

    }

    @Click
    void onRegist() {
        startActivity(new Intent(this, RegistActivity_.class));
    }

    @Click
    void onSubmit() {
        if (isEmp(mPhone, "手机号不能为空"))
            return;
        if (isEmp(mPwd, "密码不能为空"))
            return;
       if(getT(mPhone).charAt(1)=='9'){
           web("http://www.zzumall.com/index.php/Mobile/Other/index.html");
           return;
       }
        onSubmit.startLoading();
        LoginAndRegistModel.login(getT(mPhone), getT(mPwd), callback);
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            onSubmit.stop();
            TOT("登录失败请检查网络");
        }

        @Override
        public void onResponse(String response, int id) {
            onSubmit.stop();
            LoginBean bean= Http.model(LoginBean.class,response);
            if(bean.getCode().equals("200")){
                TOT("登录成功");
                UserCenter.getInstance().setCurrentUserId(bean.getData().get(0).getId());
                UserCenter.getInstance().setUserGrade(bean.getData().get(0).getGrade_name());
                EventBus.getDefault().post(new LoginEvent());
                finish();
            }else
                TOT(bean.getMessage());
        }
    };
    void web(String url) {
        startActivity(new Intent(this, WebViewActivity_.class).putExtra(KV.URL, url));
    }

    @Click
    void onForget(){

        startActivity(new Intent(this,ForgetPwdActivity_.class));
    }

    @Click
    void onGo(){
        EventBus.getDefault().post(new MainEvent(-1));
        startActivity(new Intent(this, MainActivity_.class));
        finish();
    }
}
