package com.ych.mall.ui.first.child;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ych.mall.R;
import com.ych.mall.adapter.FirstPagerFragmentAdapter;
import com.ych.mall.event.MallAndTravelEvent;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.second.child.SortFragment;
import com.ych.mall.widget.NoChangeViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ych on 2016/9/1.
 */
public class ViewPagerFragment extends BaseFragment {
    private NoChangeViewPager viewPager;

    public static ViewPagerFragment newInstance() {
        Bundle bundle = new Bundle();
        ViewPagerFragment fragment = new ViewPagerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_viewpager, null);
        EventBus.getDefault().register(this);
        initView(view);
        return view;
    }

    private void initView(View view) {
        viewPager = (NoChangeViewPager) view.findViewById(R.id.viewPager);
        viewPager.setScrollble(false);
        viewPager.setAdapter(new FirstPagerFragmentAdapter(getChildFragmentManager()));
    }

    //点击切换
    @Subscribe
    public void OnEvent(MallAndTravelEvent e) {

        if (e.getCurrntType() == MallAndTravelEvent.TYPE_CHANGE) {
            viewPager.setScrollble(true);
            viewPager.setCurrentItem(e.position, false);
            if (e.position==0)
                SortFragment.mType=SortFragment.MALL;
            else
                SortFragment.mType=SortFragment.TRAVEL;
            viewPager.setScrollble(false);
        } else if(e.getCurrntType()==MallAndTravelEvent.TYPE_GOODS){
            start(GoodsListFragment.newInstance("46",0));
        }else{
            start(TravelListFragment.newInstance("12",0));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
