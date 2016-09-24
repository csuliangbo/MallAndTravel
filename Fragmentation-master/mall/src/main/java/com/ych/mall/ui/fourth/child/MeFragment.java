package com.ych.mall.ui.fourth.child;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.ych.mall.R;

import com.ych.mall.bean.MeItemBean;
import com.ych.mall.bean.UserCenterBean;
import com.ych.mall.bean.UserInfoBean;
import com.ych.mall.event.LoginEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.RModelListenerImpForMe;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;

import com.ych.mall.ui.BuyVipActivity;
import com.ych.mall.ui.BuyVipActivity_;
import com.ych.mall.ui.LoginActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.fourth.MyinfoActivity_;
import com.ych.mall.utils.UserCenter;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/8.
 */
@EFragment(R.layout.fragment_me)
public class MeFragment extends BaseFragment {

    public static MeFragment newInstance() {
        Bundle bundle = new Bundle();
        MeFragment fragment = new MeFragment_();
        fragment.setArguments(bundle);
        return fragment;

    }
    @ViewById
    RecyclerView mGrid;
    @ViewById
    TextView mName, mID, mGrade, mIntegral;
    @ViewById
    FrameLayout onComment;
    String[] iName = new String[]{
            "个人信息", "账户管理", "会员等级", "订单管理", "我的收藏", "分享掌中游", "我的足迹"
    };
    //图片集
    int[] iImg = new int[]{
            R.drawable.ic_me_item1,
            R.drawable.ic_me_item2,
            R.drawable.ic_me_item3,
            R.drawable.ic_me_item4,
            R.drawable.ic_me_item5,
            R.drawable.ic_me_item6,
            R.drawable.ic_me_item7,
    };


    @Click
    void onIntegration() {
        if (judge())
            return;
        start(IntegralFragment.newInstance());
    }

    @Click
    void onIvHead() {
        if (judge())
            return;
        goInfo();
    }

    @Click
    void onShare() {
        if (judge())
            return;
        start(ShareFragment.newInstance());
    }

    @Click
    void onWait() {

        if (judge())
            return;
        goOrder(OrderFragment.TYPE_WAIT);
    }

    @Click
    void onPay() {

        if (judge())
            return;
        goOrder(OrderFragment.TYPE_PAY);
    }

    @Click
    void onComment() {

        if (judge())
            return;
        goOrder(OrderFragment.TYPE_COMMENT);
    }

    @Click
    void onAll() {
        if (judge())
            return;
        goOrder(OrderFragment.TYPE_ALL);

    }

    @AfterViews
    public void initViews() {
        EventBus.getDefault().register(this);
        onComment.setVisibility(View.GONE);
        if (!UserCenter.getInstance().isLoggin())
            mName.setText("请登录");
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 3);
        RecyclerViewNormalModel<MeItemBean> model = new RecyclerViewNormalModel<>(getActivity(), imp, mGrid, R.layout.item_me);
        model.init(manager);
        UserInfoModel.userCenter(infoCallBack);
    }


    private void onGridClick(int position) {
        if (judge())
            return;
        if (position == iImg[0]) {
            goInfo();
        }
        if (position == iImg[1]) {
            start(AccountFragment.newInstance());
        }
        if (position == iImg[2]) {
            start(VipGradeFragment.newInstance());

        }
        if (position == iImg[3]) {
            goOrder(OrderFragment.TYPE_ALL);
        }
        if (position == iImg[4]) {
            start(MyCollectFragment.newInstance());
        }
        if (position == iImg[5]) {
            start(ShareFragment.newInstance());
        }
        if (position == iImg[6]) {
            start(MyFootFragment.newInstance());
        }

    }

    private boolean judge() {
        if (!UserCenter.getInstance().isLoggin()) {
            AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
            builder.setTitle("登录");
            builder.setMessage("请登录后使用");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getActivity(), LoginActivity_.class));
                }
            });
            builder.show();
            return true;
        }
        if (UserCenter.getInstance().isTourist()) {
            startActivity(new Intent(getActivity(), BuyVipActivity_.class));
            return true;
        }
        return false;
    }

    //跳转个人信息界面
    private void goInfo() {
        startActivity(new Intent(getActivity(), MyinfoActivity_.class));
    }

    private void goOrder(int type) {
        start(OrderFragment.newInstance(type));
    }

    StringCallback infoCallBack = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("网络连接失败");
        }

        @Override
        public void onResponse(String response, int id) {

            UserCenterBean bean = Http.model(UserCenterBean.class, response);
            if (bean.getCode().equals("200")) {
                UserCenterBean.UserCenterData data = bean.getData().get(0);
                mName.setText(data.getMobile());
                mID.setText("ID:" + data.getId());
                mGrade.setText("等级:" + data.getGrade_name());
                mIntegral.setText(data.getIntegral());
            }
        }
    };

    RModelListenerImpForMe imp = new RModelListenerImpForMe() {
        @Override
        public List<MeItemBean> getList(String str) {
            List<MeItemBean> datas = new ArrayList<>();
            for (int i = 0; i < iName.length; i++) {
                datas.add(new MeItemBean(iName[i], iImg[i]));
            }
            return datas;
        }

        @Override
        public void covert(YViewHolder holder, MeItemBean meItemBean) {
            holder.setText(R.id.name, meItemBean.getName());
            holder.setImgRes(R.id.img, meItemBean.getImg());
            final int po = meItemBean.getImg();
            holder.getCovertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGridClick(po);
                }
            });
        }
    };

    @Subscribe
    public void onEvent(LoginEvent e){
        UserInfoModel.userCenter(infoCallBack);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
