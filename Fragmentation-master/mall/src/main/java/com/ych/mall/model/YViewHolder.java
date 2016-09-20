package com.ych.mall.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by ych on 2016/9/5.
 */
public class YViewHolder extends RecyclerView.ViewHolder {
    View mView;

    public YViewHolder(View itemView) {
        super(itemView);
        this.mView = itemView;
    }

    public View getCovertView() {
        return mView;
    }

    public <T extends View> T getView(int viewId) {
        View view = mView.findViewById(viewId);
        return (T) view;
    }

    public void setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);

    }

    public void setImgRes(int viewId, int res) {
        ImageView iv = getView(viewId);
        iv.setImageResource(res);
    }

    public void loadImg(Context context, int viewId, String url) {
        ImageView iv = getView(viewId);
        Glide.with(context).load(url).into(iv);
    }
}
