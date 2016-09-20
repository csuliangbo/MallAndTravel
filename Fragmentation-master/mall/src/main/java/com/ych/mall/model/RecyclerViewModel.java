package com.ych.mall.model;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/5.
 */
public class RecyclerViewModel<T> implements SwipeRefreshLayout.OnRefreshListener {
    public interface RModelListener<T> {
        /**
         * 获取数据
         *
         * @param callback StringCallback
         * @param page     当前页
         */
        void getData(StringCallback callback, int page);

        /**
         * 获取数据失败
         *
         * @param state RecycelView当前状态
         */
        void onErr(int state);

        /**
         * 解析数据成实体类
         *
         * @param str json字符串
         * @return 解析得到的实体类
         */
        List<T> getList(String str);

        /**
         * 数据绑定item
         *
         * @param holder ViewHolder
         * @param t      实体类
         */
        void covert(YViewHolder holder, T t);
    }

    private RModelListener mListener;
    /**
     * 最小数据量（小于显示没有更多数据）
     */
    private int miniSize = 5;
    /**
     * 新建RecyclerView
     */
    public static final int STATE_NEW = 12;
    /**
     * 下拉加载状态
     */
    public static final int STATE_LOAD = 13;
    /**
     * 上拉刷新状态
     */
    public static final int STATE_REFRESH = 14;
    /**
     * RecyclerView当前状态
     */
    private int CURRENT_STATE = 12;
    /**
     * 数据源
     */
    public List<T> dataList;
    private int page = 0;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    //上拉刷新是否加载完
    private boolean isLoadOver = true;
    private int mLayout;
    private ParentRecyclerViewAdapter<T> mAdapter;

    private List<T> mTempList = new ArrayList<>();

    public RecyclerViewModel(Context context, RModelListener listener, RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout, int layout) {
        this.mRecyclerView = recyclerView;
        this.mSwipeRefreshLayout = swipeRefreshLayout;
        mListener = listener;
        mLayout = layout;
        mContext = context;

    }

    public void setMiniSize(int miniSize) {
        this.miniSize = miniSize;
    }

    public void init() {
        mLayoutManager = new LinearLayoutManager(mContext);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorPrimary, R.color.text_blue, R.color.text_red, R.color.text_orange);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();

                //lastVisibleItem >= totalItemCount - 1 表示剩下1个item自动加载，自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 1 && dy > 0) {
                    if (isLoadOver) {

                        CURRENT_STATE = STATE_LOAD;
                        isLoadOver = false;
                        mListener.getData(callback, page);

                    }
                }

            }
        });
        mListener.getData(callback, page);
    }

    @Override
    public void onRefresh() {
        page = 0;
        CURRENT_STATE = STATE_REFRESH;
        mListener.getData(callback, page);
    }

    public void noti(){
        mAdapter.notifyDataSetChanged();
    }

    public void stopRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);

    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            mListener.onErr(CURRENT_STATE);
            if (CURRENT_STATE == STATE_REFRESH)
                stopRefresh();
        }

        @Override
        public void onResponse(String response, int id) {
            Log.i("list", CURRENT_STATE + "/" + response);
            switch (CURRENT_STATE) {
                case STATE_LOAD:
                    mTempList.clear();
                    try {
                        mTempList.addAll(mListener.getList(response));
                    } catch (Exception e) {
                        isLoadOver = false;
                    }

                    if (mTempList.size() <= miniSize) {

                        isLoadOver = false;
                    } else {

                        isLoadOver = true;
                    }
                    dataList.addAll(mTempList);
                    mAdapter.notifyDataSetChanged();
                    page++;
                    break;
                case STATE_NEW:

                    dataList = mListener.getList(response);
                    if (dataList == null)
                        return;
                    if (dataList.size() <= miniSize)
                        isLoadOver = false;
                    mAdapter = new ParentRecyclerViewAdapter<T>(mContext, mLayout, dataList, mListener);
                    mRecyclerView.setAdapter(mAdapter);
                    page++;
                    break;

                case STATE_REFRESH:

                    if (dataList != null) {
                        dataList.clear();
                        List<T> getLists = mListener.getList(response);
                        if (getLists == null) {
                            isLoadOver = false;

                        }else {
                            dataList.addAll(getLists);
                            if (getLists.size() <= miniSize)
                                isLoadOver = false;
                            else
                                isLoadOver = true;
                        }
                        mAdapter.notifyDataSetChanged();
                        page++;
                    }
                    stopRefresh();
                    break;
            }
        }
    };

}
