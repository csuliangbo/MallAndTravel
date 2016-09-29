package com.ych.mall.ui;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.UserInfoBean;
import com.ych.mall.event.AddressEvent;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.PatternUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Administrator on 2016/9/28.
 */
@EActivity(R.layout.activity_contact)
public class ContactActivity extends BaseActivity {
    @ViewById(R.id.et_contact_name)
    EditText etContactName;
    @ViewById(R.id.et_contact_phone)
    EditText etContactPhone;
    @ViewById(R.id.tiTitle)
    TextView tiTitle;
    private String name;
    private String phone;

    @Click
    void onBack() {
        finish();
    }

    @Click
    void onConfirm() {
        name = etContactName.getText().toString();
        phone = etContactPhone.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
            TOT("请输入信息");
            return;
        }
        if (PatternUtil.isMobileNO(phone)) {
            TOT("手机号格式不正确，请重新输入");
            etContactPhone.setText("");
            return;
        }
        EventBus.getDefault().post(new AddressEvent("", name, phone));
        TOT("修改成功");
        finish();
    }

    @AfterViews
    void init() {
        tiTitle.setText("修改联系方式");
        etContactName.setText(getIntent().getStringExtra("ContactName"));
        etContactPhone.setText(getIntent().getStringExtra("ContactPhone"));
    }

}
