package com.ych.mall.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lljjcoder.citypickerview.widget.CityPickerView;
import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.UserInfoBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseActivity;
import com.ych.mall.utils.KV;
import com.ych.mall.widget.ClearEditText;
import com.ych.mall.widget.ProgressButton;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/7.
 */
@EActivity(R.layout.activity_new_address)
public class NewAddressActivity extends BaseActivity {
    @ViewById
    TextView tiTitle;
    @ViewById
    TextView tiText;
    @ViewById
    ClearEditText zipCode;
    @ViewById
    ClearEditText mReceiver;
    @ViewById
    ClearEditText mPhone;
    @ViewById
    TextView onCityPicker;
    @ViewById
    ClearEditText mAdressDetail;
    @ViewById
    CheckBox mDefault;
    @ViewById
    ProgressButton onSubmit;
    String province;
    String city;
    String district;
    String addID;

    boolean newAdd = true;

    @Click
    public void onCityPicker() {
        hideSoftKeyBord();
        CityPickerView cityPickerView = new CityPickerView(this);
        cityPickerView.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                province = citySelected[0];
                //城市
                city = citySelected[1];
                //区县
                district = citySelected[2];
                //邮编
                String code = citySelected[3];
                onCityPicker.setText(province + city + district);
                zipCode.setText(code);
            }
        });
        cityPickerView.setTextColor(Color.BLACK);//新增文字颜色修改
        cityPickerView.setTextSize(20);//新增文字大小修改
        cityPickerView.setVisibleItems(6);//新增滚轮内容可见数量
        cityPickerView.setIsCyclic(true);//滚轮是否循环滚动
        cityPickerView.show();
    }

    @Click
    public void onBack() {
        finish();
    }

    String d;

    @Click
    void onSubmit() {
        if (isEmp(mReceiver, "收货人不能为空"))
            return;
        if (isEmp(mPhone, "收货人不能为空"))
            return;
        if (isEmp(onCityPicker, "地址不能为空"))
            return;
        if (isEmp(mAdressDetail, "地址不能为空"))
            return;
        if (isEmp(zipCode, "邮编不能为空"))
            return;

        onSubmit.startLoading();
        if (mDefault.isChecked())
            d = "1";
        else
            d = "0";
        if (newAdd)
            UserInfoModel.addAddress(getT(mReceiver), province, city, district, getT(mAdressDetail), d, getT(mPhone), getT(zipCode), callback);
        else
            UserInfoModel.editAddress(addID, getT(mReceiver), province, city, district, getT(mAdressDetail), d, getT(mPhone), getT(zipCode), callback);
    }

    UserInfoBean.UserInfoData mData;

    @AfterViews
    public void initViews() {
        tiTitle.setText("新增地址");
        tiText.setVisibility(View.GONE);
        onSubmit.setText("保存", "提交中...");
        if (getIntent().getSerializableExtra(KV.DATA) == null) {
            newAdd = true;

        } else {
            newAdd = false;
            tiText.setVisibility(View.VISIBLE);
            tiText.setText("删除");

            tiTitle.setText("修改地址");
            mData = (UserInfoBean.UserInfoData) getIntent().getSerializableExtra(KV.DATA);
            tiText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NewAddressActivity.this);
                    builder.setTitle("提示");
                    builder.setMessage("确定删除此地址？");
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onSubmit.startLoading();
                            UserInfoModel.delAddress(delCallBack,mData.getAddressid());
                        }
                    });
                    builder.show();
                }
            });
            mReceiver.setText(mData.getAddressrealname());
            mPhone.setText(mData.getAddressmobile());
            onCityPicker.setText(mData.getProv() + mData.getCity() + mData.getDist());
            province = mData.getProv();
            city = mData.getCity();
            district = mData.getDist();
            mAdressDetail.setText(mData.getAddress());
            zipCode.setText(mData.getZipcode());
            addID = mData.getAddressid();
            if (mData.getStatus().equals("1"))
                mDefault.setChecked(true);
            else
                mDefault.setChecked(false);
        }
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            onSubmit.stop();
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            onSubmit.stop();
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                UserInfoBean b = new UserInfoBean();
                UserInfoBean.UserInfoData data = b.new UserInfoData();
                data.setAddressrealname(getT(mReceiver));
                data.setAddressmobile(getT(mPhone));
                data.setAddress(getT(mAdressDetail));
                if (newAdd)
                    data.setStatus(d);
                else
                    data.setStatus("1");
                EventBus.getDefault().post(data);
                finish();
            }
            TOT(bean.getMessage());
        }
    };

    StringCallback delCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            onSubmit.stop();
        }

        @Override
        public void onResponse(String response, int id) {

            onSubmit.stop();
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")){
                UserInfoBean b = new UserInfoBean();
                UserInfoBean.UserInfoData data = b.new UserInfoData();
                data.setStatus("1");
                EventBus.getDefault().post(data);
                finish();}
        }
    };
}
