package com.ych.mall.wxapi;


import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ych.mall.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    public static String PAY_STATE = "01";

    private IWXAPI api;
    private TextView tvTitle;
    private TextView tvConfirm;
    private LinearLayout llSucced;
    private LinearLayout llFail;
    private ImageView onBack;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_pay_result);
        initWidght();
        api = WXAPIFactory.createWXAPI(this, "wxc60cf9d8efdbbd50");
        api.handleIntent(getIntent(), this);
    }

    private void initWidght() {
        tvTitle = (TextView) findViewById(R.id.tiTitle);
        onBack = (ImageView) findViewById(R.id.onBack);
        llFail = (LinearLayout) findViewById(R.id.ll_fail);
        llSucced = (LinearLayout) findViewById(R.id.ll_succed);
        tvConfirm = (TextView) findViewById(R.id.confirm);
        onBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (String.valueOf(resp.errCode).equals("0")) {
                PAY_STATE = "00";
            } else if (String.valueOf(resp.errCode).equals("-1")) {
                llFail.setVisibility(View.VISIBLE);
                llSucced.setVisibility(View.GONE);
                PAY_STATE = "02";
            } else if (String.valueOf(resp.errCode).equals("-2")) {
                llFail.setVisibility(View.VISIBLE);
                llSucced.setVisibility(View.GONE);
                PAY_STATE = "03";
            }
        }
    }
}