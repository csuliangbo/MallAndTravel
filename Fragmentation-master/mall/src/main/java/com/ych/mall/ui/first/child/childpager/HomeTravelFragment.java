package com.ych.mall.ui.first.child.childpager;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ych.mall.R;
import com.ych.mall.adapter.HomeTravelAdapter;
import com.ych.mall.event.MainEvent;
import com.ych.mall.event.MallAndTravelEvent;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.widget.ClearEditText;

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
public class HomeTravelFragment extends BaseFragment {


    @ViewById(R.id.rv_list)
    RecyclerView list;
    @ViewById
    ClearEditText mSearch;

    public static HomeTravelFragment newInStance() {
        Bundle bundle = new Bundle();
        HomeTravelFragment fragment = new HomeTravelFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    String[] s1 = new String[]{"http://www.zzumall.com/Public/Uploads/ad/2016-08-19/57b676740cd4c.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-08-19/57b676683d6fe.jpg", "http://www.zzumall.com/Public/Uploads/ad/2016-08-16/57b270c55393a.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-08-19/57b6702a0516a.jpg",
            "http://www.zzumall.com/Public/Uploads/ad/2016-08-19/57b6703693079.jpg", "http://www.zzumall.com/Public/Uploads/ad/2016-08-19/57b6703b50ce6.jpg"
            , "http://www.zzumall.com/Public/Uploads/ad/2016-08-30/57c542b00bdf9.jpg"
    };
    String[] s2 = new String[]{"", "", "http://www.zzumall.com/Public/Uploads/class/2016-08-24/57bd0ee6565ec.jpg", "http://www.zzumall.com/Public/Uploads/class/2016-08-17/57b42cd38eb05.jpg"
            , "http://www.zzumall.com/Public/Uploads/class/2016-08-15/57b162ea9ceb6.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-09-02/57c91bd5a85cd.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-08-17/57b418831540b.jpg",
            "http://www.zzumall.com/Public/Uploads/class/2016-08-18/57b5887d84b64.jpg"};

    @AfterViews
    public void ininList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layoutManager);
        list.setAdapter(new HomeTravelAdapter(getActivity(), s2, s1));
    }

    @Click
    void onClass() {
        EventBus.getDefault().post(new MainEvent());
    }

    @Click
    void onSearch() {
        hideSoftKeyBord();
        ((SupportFragment) getParentFragment()).start(SearchFragment.newInstance(mSearch.getText().toString(), GoodsFragment.TYPE_TRAVEL));
    }
}