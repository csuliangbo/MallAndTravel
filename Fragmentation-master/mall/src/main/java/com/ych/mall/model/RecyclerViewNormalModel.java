package com.ych.mall.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ych.mall.bean.ParentBean;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/8.
 */
public class RecyclerViewNormalModel<T> {
    private Context mContext;
    private RecyclerViewModel.RModelListener mListener;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    public List<T> dataList;
    private int mLayout;
    private ParentRecyclerViewAdapter<T> mAdapter;

    public void dataChange() {
        mAdapter.notifyDataSetChanged();
    }

    public List<T> getData() {
        return dataList;
    }

    public RecyclerViewNormalModel(Context context, RecyclerViewModel.RModelListener listener, RecyclerView recyclerView, int layout) {
        this.mRecyclerView = recyclerView;
        mListener = listener;
        mLayout = layout;
        mContext = context;
    }

    public void init(RecyclerView.LayoutManager manager) {
        mLayoutManager = manager;
        mRecyclerView.setLayoutManager(mLayoutManager);
        mListener.getData(callback, -1);

    }

    public void initNoStart(RecyclerView.LayoutManager manager) {
        mLayoutManager = manager;
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    public void refresh() {
        mListener.getData(callback, -1);
    }

    public void reset() {
        if (dataList==null)
            return;
        dataList.clear();
        mAdapter.notifyDataSetChanged();
    }

    public void insertData(T t) {
        dataList.add(0, t);
        mAdapter.notifyDataSetChanged();
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            dataList = mListener.getList(response);
            if (dataList == null)
                return;
            mAdapter = new ParentRecyclerViewAdapter<T>(mContext, mLayout, dataList, mListener);
            mRecyclerView.setAdapter(mAdapter);
        }
    };


}
