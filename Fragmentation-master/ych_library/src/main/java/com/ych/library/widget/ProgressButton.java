package com.ych.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ych.ychwidgetlibrary.R;


/**
 * Created by ych on 2016/9/12.
 */
public class ProgressButton extends FrameLayout {
    //正常状态
    final int STATUS_NORMAL = 12;
    //错误
    final int STATUS_ERR = 13;
    //加载中
    final int STATUS_LOADING = 14;
    int currentStatus = STATUS_NORMAL;

    Context mContext;
    TextView mTv;
    ProgressWheel mWheel;
    View mErrView;
    boolean isErrShake = true;
    //普通显示文本
    String mText;
    //加载中文本
    String mLoadText;
    //错误显示文本
    String mErrText = "error";
    PBClickListener mListener;

    Drawable mErrDrawale;

    //点击监视
    interface PBClickListener {
        void onClick();
    }

    float textSize = 0;

    public ProgressButton(Context context) {
        this(context, null);
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.ProgressButton);
        mErrDrawale = a.getDrawable(R.styleable.ProgressButton_err_background);
        init();
    }

    private void init() {
        mErrView = new View(mContext);
        LayoutParams errParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mErrView.setLayoutParams(errParams);
        if (mErrDrawale == null)
            mErrView.setBackgroundColor(Color.parseColor("#EE411E"));
        else
            mErrView.setBackground(mErrDrawale);
        mErrView.setVisibility(GONE);
        addView(mErrView);

        mTv = new TextView(mContext);
        LayoutParams tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tvParams.gravity = Gravity.CENTER;
        mTv.setLayoutParams(tvParams);
        if (textSize != 0)
            mTv.setTextSize(textSize);
        addView(mTv);

        mWheel = new ProgressWheel(mContext);
        LayoutParams vParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        vParams.rightMargin = 20;
        vParams.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        mWheel.setLayoutParams(vParams);
        mWheel.setBarColor(Color.WHITE);
        mWheel.setBarWidth(5);
        mWheel.setCircleRadius(55);
        mWheel.spin();
        mWheel.setVisibility(GONE);
        addView(mWheel);


        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentStatus) {
                    case STATUS_NORMAL:

                        break;
                    case STATUS_ERR:
                        backToNormal();
                        break;
                    case STATUS_LOADING:

                        break;
                }
                mListener.onClick();

            }
        });
    }

    /**
     * 加载圆颜色
     */
    public void setWheelColor(int color) {
        mWheel.setBarColor(color);
    }

    /**
     * 加载圆半径
     */
    public void setWheelRadius(int radius) {
        mWheel.setCircleRadius(radius);
    }

    /**
     * 设置点击事件
     */
    public void setOnClick(OnClickListener click) {
        this.setOnClickListener(click);
    }

    /**
     * 设置显示文字
     *
     * @param text 显示文字
     */
    public void setText(String text) {
        mText = text;
        mTv.setText(mText);
    }

    /**
     * 设置显示文字和加载文字
     *
     * @param text     显示文字
     * @param loadText 加载文字
     */
    public void setText(String text, String loadText) {
        setText(text);
        mLoadText = loadText;
    }

    /**
     * 设置显示文字，加载文字和错误文字
     *
     * @param text     显示文字
     * @param loadText 加载文字
     * @param errText  错误文字
     */
    public void setText(String text, String loadText, String errText) {
        setText(text, loadText);
        mErrText = errText;
    }

    /**
     * 设置错误提示文字
     *
     * @param errText
     */
    public void setErrText(String errText) {
        mErrText = errText;
    }

    /**
     * 设置文字颜色
     *
     * @param color
     */
    public void setTextColor(int color) {
        mTv.setTextColor(color);
    }

    /**
     * 设置错误背景
     *
     * @param drawale
     */
    public void setErrDrawale(Drawable drawale) {
        mErrView.setBackground(drawale);
    }

    /**
     * 设置错误背景颜色
     *
     * @param color
     */
    public void setErrColor(@ColorInt int color) {
        mErrView.setBackgroundColor(color);
    }

    /**
     * 开始加载
     */
    public void startLoading() {
        if (currentStatus == STATUS_ERR)
            backToNormal();
        mWheel.setVisibility(View.VISIBLE);
        if (mLoadText != null)
            mTv.setText(mLoadText);
        currentStatus = STATUS_LOADING;
    }

    /**
     * 停止加载
     */
    public void stop() {
        if (currentStatus == STATUS_ERR)
            backToNormal();
        currentStatus = STATUS_NORMAL;
        mWheel.setVisibility(GONE);
        mTv.setText(mText);
    }

    /**
     * 显示错误
     */
    public void err() {
        if (mErrText != null)
            mTv.setText(mErrText);
        if (isErrShake)
            shake();
        mErrView.setVisibility(VISIBLE);
        if (currentStatus == STATUS_LOADING) {
            mWheel.setVisibility(GONE);
        }
        currentStatus = STATUS_ERR;
    }

    //回复正常状态
    private void backToNormal() {
        mTv.setText(mText);
        mErrView.setVisibility(GONE);
        currentStatus = STATUS_NORMAL;
    }

    public void setPBClickListener(PBClickListener listener) {
        mListener = listener;
    }

    /**
     * 设置晃动动画
     */
    public void shake() {
        this.setAnimation(shakeAnimation(2));
    }


    /**
     * 晃动动画
     *
     * @param counts 1秒钟晃动多少
     * @return
     */
    private Animation shakeAnimation(int counts) {
        Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
        translateAnimation.setInterpolator(new CycleInterpolator(counts));
        translateAnimation.setDuration(500);
        return translateAnimation;
    }

    public void setErrShake(boolean shake) {
        isErrShake = shake;
    }

}
