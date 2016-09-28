package com.ych.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by ych on 2016/9/26.
 */
public class MyScrollView extends ScrollView {
    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static interface OnBorderListener {

        /**
         * Called when scroll to bottom
         */
        public void onBottom();

        /**
         * Called when scroll to top
         */
        public void onTop();
    }


    View contentView ;

    private OnBorderListener mListener;

    public void setmListener(OnBorderListener mListener) {
        this.mListener = mListener;
    }

    private void doOnBorderListener() {
        if (contentView==null)
        contentView=getChildAt(0);

        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (mListener != null) {
                mListener.onBottom();
            }
        } else if (getScrollY() == 0) {
            if (mListener != null) {
                mListener.onTop();
            }
        }
    }


    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        doOnBorderListener();
    }


}
