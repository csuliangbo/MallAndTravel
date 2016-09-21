package com.ych.mall.ui.first.child.childpager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.View;
import android.widget.Toast;

import com.ych.mall.R;
import com.ych.mall.adapter.HomeMallAdapter;
import com.ych.mall.adapter.HomeTravelAdapter;
import com.ych.mall.event.MainEvent;
import com.ych.mall.event.MallAndTravelEvent;
import com.ych.mall.ui.LoginActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.widget.ClearEditText;
import com.ych.mall.widget.SlideShowView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by ych on 2016/8/31.
 */
@EFragment(R.layout.fragment_home_travel)
public class HomeMallFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @ViewById(R.id.refresh_layout)
    SwipeRefreshLayout rLayout;
    @ViewById(R.id.rv_list)
    RecyclerView list;
    @ViewById
    ClearEditText mSearch;

    public static HomeMallFragment newInstance() {
        Bundle bundle = new Bundle();
        HomeMallFragment fragment = new HomeMallFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    String[] s1 = new String[]{
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-05/57cd1599cbf79.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-05/57cccc06451da.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-03/57ca6b940c3bb.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-02/57c8dcafc95db.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-02/57c8dc6555b73.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-03/57ca9217d80aa.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-09-01/57c7e83b6b5d2.jpg"
    };
    String[] s2 = new String[]{"", "",
            "http://www.zzumall.com/Public/Uploads/class/2016-09-03/57ca6f411bed8.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-08-22/57baa1949ea42.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-09-03/57ca8ecda3a62.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-09-05/57ccd95265da9.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-09-03/57ca46117bbc0.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-08-24/57bd0ee6565ec.jpg"};
    LinearLayoutManager layoutManager;

    @AfterViews
    public void ininList() {
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
        list.setAdapter(new HomeMallAdapter(getActivity(), s2, s1));
        rLayout.setColorSchemeColors(R.color.colorPrimary, R.color.text_blue, R.color.text_red, R.color.text_orange);
        rLayout.setOnRefreshListener(this);
        list.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载，l各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
//                    if(isLoadingMore){
//                        Log.d(TAG,"ignore manually update!");
//                    } else{
//                        loadPage();//这里多线程也要手动控制isLoadingMore
//                        isLoadingMore = false;
//                    }

                }
            }
        });
    }


    @Override
    public void onRefresh() {

    }

    @Click
    void onLogin() {
        getActivity().startActivity(new Intent(getActivity(), LoginActivity_.class));
    }

    @Click
    void onClass() {
        EventBus.getDefault().post(new MainEvent());
    }

    @Click
    void onSearch() {
        hideSoftKeyBord();
        ((SupportFragment) getParentFragment()).start(SearchFragment.newInstance(mSearch.getText().toString(), GoodsFragment.TYPE_GOODS));
    }
}
