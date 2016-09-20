package com.ych.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ych.mall.R;

/**
 * Created by ych on 2016/9/12.
 */
public class ProgressButton extends FrameLayout {

    Context mContext;
    TextView mTv;
    ProgressWheel mWheel;
    String mText;
    String mLoadText;

    public ProgressButton(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;

        init();
    }

    private void init() {
        LayoutInflater.from(mContext).inflate(R.layout.widget_progress_button, this, true);
        mTv = (TextView) findViewById(R.id.wp_text);
        mWheel = (ProgressWheel) findViewById(R.id.wp_wheel);
    }

    public void setOnClick(OnClickListener click) {
        this.setOnClickListener(click);
    }

    public void setText(String text, String loadText) {
        mText = text;
        mLoadText = loadText;
        mTv.setText(mText);
    }

    public void startLoading() {
        mWheel.setVisibility(View.VISIBLE);
        mTv.setText(mLoadText);
    }

    public void stop() {
        mWheel.setVisibility(GONE);
        mTv.setText(mText);
    }
}

