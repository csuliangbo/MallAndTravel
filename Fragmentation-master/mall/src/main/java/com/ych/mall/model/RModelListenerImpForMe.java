package com.ych.mall.model;

import com.ych.mall.R;
import com.ych.mall.bean.MeItemBean;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

/**
 * Created by ych on 2016/9/9.
 */
public class RModelListenerImpForMe implements RecyclerViewModel.RModelListener<MeItemBean> {
    @Override
    public void getData(StringCallback callback, int page) {
        callback.onResponse("ok", 0);
    }

    @Override
    public void onErr(int state) {

    }

    @Override
    public List<MeItemBean> getList(String str) {
        return null;
    }

    @Override
    public void covert(YViewHolder holder, MeItemBean meItemBean) {

    }
}
