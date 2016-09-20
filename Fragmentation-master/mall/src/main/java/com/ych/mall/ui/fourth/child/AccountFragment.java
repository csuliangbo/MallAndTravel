package com.ych.mall.ui.fourth.child;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.AccountBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.fourth.BalanceActivity_;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/9.
 */
@EFragment(R.layout.fragment_my_account)
public class AccountFragment extends BaseFragment {
    public static AccountFragment newInstance() {
        Bundle bundle = new Bundle();
        AccountFragment fragment = new AccountFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById
    TextView tiTitle,mMoney,mIntegral,mCardNum,mTicket;

    @Click
    void onBack() {
        back();
    }

    @AfterViews
    void initViews() {
        tiTitle.setText("账户管理");
        UserInfoModel.accountManage(callback);
    }

    @Click
    void onBalance() {
        startActivity(new Intent(getActivity(), BalanceActivity_.class));
    }

    @Click
    void onBankCard() {
        start(BankCardFragment.newInstance());
    }

    @Click
    void onIntegral() {
        start(IntegralFragment.newInstance());

    }

    @Click
    void onTicket() {
        //start(TicketFragment.newInstance());
    }

    StringCallback callback=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {
            AccountBean bean= Http.model(AccountBean.class,response);
            if (bean.getCode().equals("200"))
            {
                AccountBean.AccountData data=bean.getData();
                mMoney.setText(data.getAccount_balance());
                mIntegral.setText(data.getIntegral());
                mCardNum.setText(data.getNum());
               // mTicket.setText(data.);
            }
            else TOT(bean.getMessage());
        }
    };
}
