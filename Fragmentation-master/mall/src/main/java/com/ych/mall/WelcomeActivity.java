package com.ych.mall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ych.mall.adapter.WecommPagerAdapter;
import com.ych.mall.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/10.
 */
public class WelcomeActivity extends BaseActivity {
    private SharedPreferences share;
    private SharedPreferences.Editor editor;
    private List<View> guides = new ArrayList<View>();
    private ViewPager pager;
    private ImageView curDot;
    // 位移量
    private int offset;
    // 记录当前的位置
    private int curPos = 0;

    // 首次使用程序的显示的欢迎图片
    private int[] ids = {R.drawable.welcome_01,
            R.drawable.welcome_02, R.drawable.welcome_03
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        share = getSharedPreferences("showWelcomm", Context.MODE_PRIVATE);
        editor = share.edit();
        // 判断是否首次登录程序
        if (share.contains("shownum")) {
            int num = share.getInt("shownum", 0);
            editor.putInt("shownum", num++);
            editor.commit();
            skipActivity(0);
        } else {
            editor.putInt("shownum", 1);
            editor.commit();
            setContentView(R.layout.activity_welcome);
            TextView tvOnEnter = (TextView) findViewById(R.id.onEnter);
            tvOnEnter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    skipActivity(0);
                }
            });
            initView();
        }

    }

    private void initView() {
        for (int i = 0; i < ids.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setImageResource(ids[i]);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(params);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            guides.add(iv);
        }
        curDot = (ImageView) findViewById(R.id.cur_dot);
        curDot.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    public boolean onPreDraw() {
                        offset = curDot.getWidth();
                        return true;
                    }
                });

        WecommPagerAdapter adapter = new WecommPagerAdapter(guides);
        pager = (ViewPager) findViewById(R.id.showwelom_page);
        pager.setAdapter(adapter);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                moveCursorTo(arg0);
                if (arg0 == ids.length - 1) {// 到最后一张了
                    skipActivity(2);
                }
                curPos = arg0;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }

        });

    }

    /**
     * 移动指针到相邻的位置
     *
     * @param position 指针的索引值
     */
    private void moveCursorTo(int position) {
        TranslateAnimation anim = new TranslateAnimation(offset * curPos,
                offset * position, 0, 0);
        anim.setDuration(300);
        anim.setFillAfter(true);
        curDot.startAnimation(anim);
    }


    /**
     * 延迟多少秒进入主界面
     *
     * @param min 秒
     */
    private void skipActivity(int min) {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this,
                        MainActivity_.class);
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        }, 1000 * min);
    }

}
