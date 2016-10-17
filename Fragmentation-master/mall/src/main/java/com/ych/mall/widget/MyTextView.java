package com.ych.mall.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by ych on 2016/10/13.
 */
public class MyTextView extends TextView{


    public MyTextView(Context context) {

        super(context);

    }



    public MyTextView(Context context, AttributeSet attrs) {

        super(context, attrs);

    }



    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

    }



    @Override
    public boolean isFocused() {

        return true;

    }
}
