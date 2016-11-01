package com.ych.mall.ui.fourth.child;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.GoodsListBean;
import com.ych.mall.bean.MessageBean;
import com.ych.mall.bean.TravelListBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.Tools;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * 消息界面
 */
@EFragment(R.layout.fragment_message)
public class MessageFragment extends BaseFragment implements RecyclerViewModel.RModelListener<MessageBean.MessageData> {
    @ViewById(R.id.recycleview_message)
    RecyclerView messageRlv;
    @ViewById(R.id.swipe)
    SwipeRefreshLayout layout;
    @ViewById(R.id.tiTitle)
    TextView tvTitle;

    public static MessageFragment newInstance() {
        Bundle bundle = new Bundle();
        MessageFragment fragment = new MessageFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Click
    void onBack() {
        back();
    }

    @AfterViews
    void init() {
        tvTitle.setText("消息");
        RecyclerViewModel<MessageBean.MessageData> rvm =
                new RecyclerViewModel<MessageBean.MessageData>(getActivity(),
                        this,
                        messageRlv,
                        layout,
                        R.layout.item_message);
        rvm.setMiniSize(3);
        rvm.init();
    }


    @Override
    public void getData(StringCallback callback, int page) {
        UserInfoModel.message(callback, page);
    }

    @Override
    public void onErr(int state) {
        TOT("网络连接错误");
    }

    @Override
    public List<MessageBean.MessageData> getList(String str) {
        MessageBean bean = Http.model(MessageBean.class, str);
        if (bean.getCode().equals("200")) {
            tvTitle.setText("消息(" + bean.getData().size() + ")");
            return bean.getData();
        }
        return null;
    }

    @Override
    public void covert(YViewHolder holder, MessageBean.MessageData messageData) {
        holder.setText(R.id.tv_content, messageData.getNews_info());
        holder.setText(R.id.tv_date, messageData.getCreate_time());
    }
}
