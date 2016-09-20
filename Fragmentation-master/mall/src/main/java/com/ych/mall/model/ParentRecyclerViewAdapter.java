package com.ych.mall.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ych.mall.bean.ParentBean;

import java.util.List;

/**
 * Created by ych on 2016/9/5.
 */
public class ParentRecyclerViewAdapter<T> extends RecyclerView.Adapter<YViewHolder> {

    private RecyclerViewModel.RModelListener mListener;


    List<T> mDataList;
    Context mContext;
    int mLayoutRes;
    private LayoutInflater mLayoutInflater;
    public ParentRecyclerViewAdapter(Context context, int layoutRes, List<T> datas,
                                     RecyclerViewModel.RModelListener listener) {
        mContext = context;
        mLayoutRes = layoutRes;
        mDataList = datas;
        mListener = listener;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public YViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YViewHolder(mLayoutInflater.inflate(mLayoutRes,parent,false));
    }

    @Override
    public void onBindViewHolder(YViewHolder holder, int position) {
        mListener.covert(holder, mDataList.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
