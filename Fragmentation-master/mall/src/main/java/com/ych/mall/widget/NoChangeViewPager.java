package com.ych.mall.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ych on 2016/9/1.
 */
public class NoChangeViewPager extends ViewPager {
    private boolean scrollble = true;

    public NoChangeViewPager(Context context) {
        super(context);
    }

    public NoChangeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void scrollTo(int x, int y){
        if (scrollble){
            super.scrollTo(x, y);
        }
    }
public void setScrollble(boolean scrollble){
    this.scrollble=scrollble;
}
}
