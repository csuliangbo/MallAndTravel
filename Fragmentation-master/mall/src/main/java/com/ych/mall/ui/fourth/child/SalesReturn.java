package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/21.
 */
@EFragment(R.layout.fragment_apply_sales_return)
public class SalesReturn extends BaseFragment {
    @ViewById(R.id.et_reason)
    EditText etReason;
    private String orderNum;
    @ViewById(R.id.tiTitle)
    TextView tvTitle;

    public static SalesReturn newInstance(String orderNum) {
        Bundle bundle = new Bundle();
        bundle.putString(KV.ORDER_NUM, orderNum);
        SalesReturn fragment = new SalesReturn_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Click
    void onBack() {
        back();
    }

    @Click
    void onCommit() {
        UserInfoModel.salesReturn(salesReturnCallback, orderNum, etReason.getText().toString());
    }

    @AfterViews
    void initData() {
        orderNum = getArguments().getString(KV.ORDER_NUM);
        tvTitle.setText("退货申请");
        Log.e("KTY orderNum", orderNum);
    }

    //退货申请
    StringCallback salesReturnCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络链接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                TOT(bean.getMessage());
                back();
            } else {
                TOT(bean.getMessage());
            }
        }
    };
}
