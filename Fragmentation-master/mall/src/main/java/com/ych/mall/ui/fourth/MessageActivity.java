package com.ych.mall.ui.fourth;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ych.mall.MainActivity_;
import com.ych.mall.R;
import com.ych.mall.bean.MessageBean;
import com.ych.mall.model.Http;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.UserInfoModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

/**
 * Created by Administrator on 2016/10/26.
 */
@EActivity(R.layout.fragment_message)
public class MessageActivity extends BaseActivity implements RecyclerViewModel.RModelListener<MessageBean.MessageData> {

    @ViewById(R.id.recycleview_message)
    RecyclerView messageRlv;
    @ViewById(R.id.swipe)
    SwipeRefreshLayout layout;
    @ViewById(R.id.tiTitle)
    TextView tvTitle;

    @Click
    void onBack() {
        back();
    }

    @AfterViews
    void init() {
        tvTitle.setText("消息");
        RecyclerViewModel<MessageBean.MessageData> rvm =
                new RecyclerViewModel<MessageBean.MessageData>(this,
                        this,
                        messageRlv,
                        layout,
                        R.layout.item_message);
        rvm.setMiniSize(3);
        rvm.init();
    }


    @Override
    public void getData(StringCallback callback, int page) {
        UserInfoModel.message(callback,page);
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

    /**
     * 判断MainActivity是否存在栈中
     *
     * @return
     */
    private boolean isExist() {
        Intent intent = new Intent(this, MainActivity_.class);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否需要调用ManActivity
     */
    private void back() {
        if (isExist()) {
            finish();
        } else {
            Intent intent = new Intent(this, MainActivity_.class);
            //清空栈
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            back();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
