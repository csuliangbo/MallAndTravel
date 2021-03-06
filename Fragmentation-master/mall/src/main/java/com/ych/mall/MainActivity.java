package com.ych.mall;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import okhttp3.Call;

import com.ych.mall.bean.UpdateBean;
import com.ych.mall.event.ClickEvent;
import com.ych.mall.event.MainEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.ui.LoginActivity_;
import com.ych.mall.ui.base.BaseLazyMainFragment;
import com.ych.mall.ui.first.FirstFragment;
import com.ych.mall.ui.first.child.ViewPagerFragment;
import com.ych.mall.ui.fourth.FourthFragment;
import com.ych.mall.ui.fourth.child.MeFragment_;
import com.ych.mall.ui.second.SecondFragment;
import com.ych.mall.ui.second.child.SortFragment_;
import com.ych.mall.ui.third.ThridFragment;
import com.ych.mall.ui.third.child.ShopCarFragment_;
import com.ych.mall.widget.BottomBar;
import com.ych.mall.widget.BottomBarTab;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.EActivity;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

@EActivity
public class MainActivity extends SupportActivity implements BaseLazyMainFragment.OnBackToFirstListener {
    public static final int FIRST = 0;
    public static final int SECOND = 1;
    public static final int THIRD = 2;
    public static final int FOURTH = 3;


    private SupportFragment[] mFragments = new SupportFragment[4];

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        if (savedInstanceState == null) {
            mFragments[FIRST] = FirstFragment.newInstance();
            mFragments[SECOND] = SecondFragment.newInstance();
            mFragments[THIRD] = ThridFragment.newInstance();
            mFragments[FOURTH] = FourthFragment.newInstance();

            loadMultipleRootFragment(R.id.fl_container, FIRST,
                    mFragments[FIRST],
                    mFragments[SECOND],
                    mFragments[THIRD],
                    mFragments[FOURTH]);
        } else {
            // 这里库已经做了Fragment恢复,所有不需要额外的处理了, 不会出现重叠问题

            // 这里我们需要拿到mFragments的引用,也可以通过getSupportFragmentManager.getFragments()自行进行判断查找(效率更高些),用下面的方法查找更方便些
            mFragments[FIRST] = findFragment(FirstFragment.class);
            mFragments[SECOND] = findFragment(SecondFragment.class);
            mFragments[THIRD] = findFragment(ThridFragment.class);
            mFragments[FOURTH] = findFragment(FourthFragment.class);
        }

        initView();
    }


    @Override
    protected FragmentAnimator onCreateFragmentAnimator() {
        return super.onCreateFragmentAnimator();
    }

    private void initView() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 011);
        }
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        mBottomBar.addItem(new BottomBarTab(this, R.drawable.home,"首页"))
                .addItem(new BottomBarTab(this, R.drawable.fbxs,"分类"))
                .addItem(new BottomBarTab(this, R.drawable.buy,"购物车"))
                .addItem(new BottomBarTab(this, R.drawable.me,"个人中心"));

        mBottomBar.setOnTabSelectedListener(new BottomBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, int prePosition) {
                showHideFragment(mFragments[position], mFragments[prePosition]);
                if (position == 1)
                    EventBus.getDefault().post(new ClickEvent(1));

            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {
                SupportFragment currentFragment = mFragments[position];


                int count = currentFragment.getChildFragmentManager().getBackStackEntryCount();
                // 如果不在该类别Fragment的主页,则回到主页;
                if (count > 1) {
                    if (currentFragment instanceof FirstFragment) {
                        currentFragment.popToChild(ViewPagerFragment.class, false);
                    } else if (currentFragment instanceof SecondFragment) {
                        currentFragment.popToChild(SortFragment_.class, false);
                    } else if (currentFragment instanceof ThridFragment) {
                        currentFragment.popToChild(ShopCarFragment_.class, false);
                    } else if (currentFragment instanceof FourthFragment) {
                        currentFragment.popToChild(MeFragment_.class, false);
                    }
                    return;
                }


                // 这里推荐使用EventBus来实现 -> 解耦
                if (count == 1) {
                    // 在FirstPagerFragment中接收, 因为是嵌套的孙子Fragment 所以用EventBus比较方便
                    // 主要为了交互: 重选tab 如果列表不在顶部则移动到顶部,如果已经在顶部,则刷新
                    // EventBus.getDefault().post(new TabSelectedEvent(position));
                }
            }
        });

        MallAndTravelModel.update("v"+getVersionCode(this),update);
    }


    @Override
    public void onBackPressedSupport() {

        super.onBackPressedSupport();
    }

    @Override
    public void onBackToFirstFragment() {
        mBottomBar.setCurrentItem(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(MainEvent e) {
        if(e.getPosition()==-1) {
            finish();
            return;
        }
        if (e.getPosition()==-2)
        {
            if (!e.isBottomStatus())
            {
                mBottomBar.setVisibility(View.GONE);
            }else
                mBottomBar.setVisibility(View.VISIBLE);
            return;
        }
        mBottomBar.setCurrentItem(e.getPosition());
        EventBus.getDefault().post(new ClickEvent(e.getPosition()));
        mBottomBar.setVisibility(View.VISIBLE);
if (e.getPosition()==0)
    mFragments[0].popToChild(ViewPagerFragment.class, false);

    }

    //版本号
    public int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    private PackageInfo getPackageInfo(Context context) {
        PackageInfo pi = null;

        try {
            PackageManager pm = context.getPackageManager();
            pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pi;
    }
    StringCallback update=new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {
            UpdateBean bean= Http.model(UpdateBean.class,response);
        try{
            if (bean.getCode().equals("200"))
            {
                final String url=bean.getData().getUrl();
                AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder.setTitle("发现有新版本");
                builder.setMessage(bean.getData().getIntro());
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        link(url);
                        finish();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        }catch (Exception e){}
        }
    };

    private void link(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
