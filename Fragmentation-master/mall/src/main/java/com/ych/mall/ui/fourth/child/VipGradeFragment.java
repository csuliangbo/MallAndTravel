package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.VipBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/22.
 */
@EFragment(R.layout.fragment_vip_grade)
public class VipGradeFragment extends BaseFragment {
    public static VipGradeFragment newInstance() {
        Bundle bundle = new Bundle();
        VipGradeFragment fragment = new VipGradeFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById
    TextView mName, mTime,mText;

    @AfterViews
    void init() {
        UserInfoModel.vipInfo(callback);
    }

    void setData(VipBean.VipData data) {
        sT(mName,"你已成为："+data.getGrade_name());
        String time=data.getVip_end();
        if (time!=null) {
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").
                    format(new java.util.Date(Long.parseLong(time) * 1000));
        sT(mTime,date);
        }else
        {
            mTime.setVisibility(View.GONE);
            mText.setVisibility(View.GONE);
        }

    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络加载失败");
        }

        @Override
        public void onResponse(String response, int id) {
            VipBean bean = Http.model(VipBean.class, response);
            if (bean.getCode().equals("200"))
                setData(bean.getData().get(0));
            else
                TOT(bean.getMessage());
        }
    };
}
