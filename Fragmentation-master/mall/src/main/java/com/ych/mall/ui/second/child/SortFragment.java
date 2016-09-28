package com.ych.mall.ui.second.child;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.SortLeftBean;
import com.ych.mall.bean.SortLeftBean.SortLeftData;
import com.ych.mall.bean.SortRightBean;
import com.ych.mall.bean.SortRightBean.SortRightData;
import com.ych.mall.event.ClickEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.first.child.GoodsListFragment;
import com.ych.mall.ui.first.child.TravelListFragment;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * Created by ych on 2016/9/7.
 */
@EFragment(R.layout.fragment_sort)
public class SortFragment extends BaseFragment {
    public static final int MALL = 12;
    public static final int TRAVEL = 13;
    public static int mType = MALL;
    private int mOldType = 0;
    @ViewById
    RecyclerView listView;
    @ViewById
    RecyclerView gridView;
    @ViewById
    TextView tiTitle;
    @ViewById
    ImageView onBack;
    @ViewById
    TextView mLoading;
    RecyclerViewNormalModel<SortLeftData> mListModel;
    RecyclerViewNormalModel<SortRightData> mGirdModel;
    String currentId = "0";

    public static SortFragment newInstance() {
        Bundle bundle = new Bundle();
        SortFragment fragment = new SortFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }


    @AfterViews
    public void initView() {

        mOldType = mType;
        EventBus.getDefault().register(this);
        onBack.setVisibility(View.GONE);
        tiTitle.setText("商品分类");
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mListModel = new RecyclerViewNormalModel<>(getActivity(), new leftListener(), listView, R.layout.item_sort_left);
        mListModel.init(manager);

        GridLayoutManager managerForGrid = new GridLayoutManager(getActivity(), 2);
        mGirdModel = new RecyclerViewNormalModel<>(getActivity(), new rightListener(), gridView, R.layout.item_sort_right);
        mGirdModel.initNoStart(managerForGrid);

    }

    class rightListener implements RecyclerViewModel.RModelListener<SortRightData> {

        @Override
        public void getData(StringCallback callback, int page) {
            if (mType == MALL)
                MallAndTravelModel.sortRight(callback, currentId);
            else
                MallAndTravelModel.sortRightTravel(callback, currentId);
        }

        @Override
        public void onErr(int state) {
            currentId = "-1";
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public List<SortRightData> getList(String str) {

            mLoading.setVisibility(View.GONE);
            SortRightBean bean = Http.model(SortRightBean.class, str);
            if (bean.getCode().equals("200"))
                return bean.getData();
            return null;
        }

        @Override
        public void covert(YViewHolder holder, SortRightData t) {
            final String id = t.getId();
            holder.setText(R.id.name, t.getClass_name());
            holder.getCovertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mType == MALL)
                        start(GoodsListFragment.newInstance(id,0));
                    else
                        start(TravelListFragment.newInstance(id,0));
                }
            });
        }
    }

    class leftListener implements RecyclerViewModel.RModelListener<SortLeftData> {
        @Override
        public void getData(StringCallback callback, int page) {
            if (mType == MALL)
                MallAndTravelModel.sortLeft(callback);
            else
                MallAndTravelModel.sortLeftTravel(callback);

        }

        @Override
        public void onErr(int state) {
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public List<SortLeftData> getList(String str) {
            if (mLoading!=null)
            mLoading.setVisibility(View.GONE);
            SortLeftBean bean = Http.model(SortLeftBean.class, str);
            if (bean.getCode().equals("200"))
                return bean.getData();
            return null;
        }

        @Override
        public void covert(YViewHolder holder, final SortLeftData sortLeftBean) {
            final SortLeftData t = sortLeftBean;
            final String id = t.getId();
            holder.setText(R.id.name, t.getClass_name());
            if (t.getSelect())
                holder.getView(R.id.line).setVisibility(View.VISIBLE);
            else
                holder.getView(R.id.line).setVisibility(View.GONE);
            holder.getCovertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentId.equals(id))
                        return;
                    List<SortLeftData> datas = mListModel.getData();
                    for (SortLeftData b : datas) {
                        b.setSelect(false);
                    }
                    t.setSelect(true);
                    mListModel.dataChange();
                    mLoading.setVisibility(View.VISIBLE);
                    currentId = id;
                    mGirdModel.reset();
                    mGirdModel.refresh();
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(ClickEvent e) {
        if (mType != mOldType) {
            mLoading.setVisibility(View.VISIBLE);
            mListModel.reset();
            mListModel.refresh();
            mGirdModel.reset();
            mOldType = mType;
        }
    }
}
