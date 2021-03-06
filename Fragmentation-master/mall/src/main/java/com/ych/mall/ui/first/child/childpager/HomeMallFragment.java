package com.ych.mall.ui.first.child.childpager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.umeng.socialize.utils.Log;
import com.ych.mall.R;
import com.ych.mall.adapter.HomeMallAdapter;
import com.ych.mall.adapter.HomeTravelAdapter;
import com.ych.mall.bean.AdBean;
import com.ych.mall.bean.HomeMallBean;
import com.ych.mall.event.MainEvent;
import com.ych.mall.event.MallAndTravelEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.BuyVipActivity_;
import com.ych.mall.ui.LoginActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.first.child.GoodsListFragment;

import com.ych.mall.ui.first.child.GoodsViewPagerFragment;

import com.ych.mall.ui.fourth.WebViewActivity_;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.widget.ClearEditText;
import com.ych.mall.widget.MyScrollView;
import com.ych.mall.widget.MyTextView;
import com.ych.mall.widget.SlideShowView;
import com.ych.mall.zxingcode.activity.CaptureActivity;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

/**
 * Created by ych on 2016/8/31.
 */
@EFragment(R.layout.fragment_home_travel)
public class HomeMallFragment extends BaseFragment implements RecyclerViewModel.RModelListener<HomeMallBean.Class> {
    //    @ViewById(R.id.refresh_layout)
//    SwipeRefreshLayout rLayout;
    @ViewById(R.id.rv_list)
    RecyclerView list;
    @ViewById
    ClearEditText mSearch;
    @ViewById
    MyScrollView mScroll;
    @ViewById
    LinearLayout mLL;

    MyTextView adText;
    public final static int REQUEST_CODE = 123;

    HomeMallBean.Class mData;
    RecyclerViewModel<HomeMallBean.Class> model;
    RecyclerViewHeader header;
    List<HomeMallBean.Banner> mBanner;
    List<HomeMallBean.Center> mCenter;
    List<HomeMallBean.Hot> mHot;

    public static HomeMallFragment newInstance() {
        Bundle bundle = new Bundle();
        HomeMallFragment fragment = new HomeMallFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }


    @AfterViews
    public void ininList() {

        header = RecyclerViewHeader.fromXml(getActivity(), R.layout.head_mall);
        header.findViewById(R.id.toTravel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MallAndTravelEvent(MallAndTravelEvent.TYPE_CHANGE, 1));
            }
        });
        adText = (MyTextView) header.findViewById(R.id.adText);
        model = new RecyclerViewModel<>(getActivity(),
                this,
                list,
                null,
                R.layout.item_home_travel_goods);

        model.initWithHead(header);
        mScroll.setmListener(new MyScrollView.OnBorderListener() {
            @Override
            public void onBottom() {
                model.onLoad();
            }

            @Override
            public void onTop() {

            }
        });
        MallAndTravelModel.getAd(callback);
    }


    @Click
    void onLogin() {

        if (UserCenter.getInstance().isLoggin())
            EventBus.getDefault().post(new MainEvent(3));
        else
            getActivity().startActivity(new Intent(getActivity(), LoginActivity_.class));
    }

    @Click
    void onScanner() {
        startScan();
    }

    /**
     * 点击扫一扫按钮，开启扫描二维码
     */
    public void startScan() {
        //检查权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            //进入到这里代表没有权限.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        } else {
            //跳转到扫一扫
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                //跳转到扫一扫
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
        }
    }

    @Click
    void onSearch() {
        hideSoftKeyBord();
        ((SupportFragment) getParentFragment()).start(SearchFragment.newInstance(mSearch.getText().toString(), GoodsFragment.TYPE_GOODS));
    }


    @Override
    public void getData(StringCallback callback, int page) {
        MallAndTravelModel.homeMall(callback, page);
    }

    @Override
    public void onErr(int state) {
        TOT("网络连接失败");
    }


    @Override
    public List<HomeMallBean.Class> getList(String str) {
        log(str);
        HomeMallBean bean = Http.model(HomeMallBean.class, str);
        if (bean.getCode().equals("200")) {
            if (mBanner == null || mCenter == null || mHot == null) {
                setBanner(bean.getData().getBanner());
                setCenter(bean.getData().getCenter());
                setHot(bean.getData().getHot());
            }
            return bean.getData().getClas();

        }
        return null;
    }

    @Override
    public void covert(YViewHolder holder, HomeMallBean.Class t) {
        final String id = t.getClass_id();
        holder.loadImg(getActivity(), R.id.it_img, Http.CLASS_PIC_URL + t.getClass_url());
        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SupportFragment) getParentFragment()).start(GoodsListFragment.newInstance(id, 0));
            }
        });
    }

    String[] bannerImg;
    String[] bannerUrl;
    SlideShowView sv;

    private void setBanner(final List<HomeMallBean.Banner> banner) {
        mBanner = banner;
        sv = (SlideShowView) header.findViewById(R.id.banner);
        final TextView toTravel = (TextView) header.findViewById(R.id.toTravel);
        ImageView bannerMid = (ImageView) header.findViewById(R.id.banner_mid);
        bannerImg = new String[mBanner.size()];
        bannerUrl = new String[mBanner.size()];
        int i = 0;
        for (HomeMallBean.Banner b : mBanner) {
            bannerImg[i] = Http.AD_PIC_URL + b.getBanner_url();
            bannerUrl[i] = b.getAd_link();
            i++;
        }
        sv.setData(bannerImg);

        sv.setListener(new SlideShowView.OnVClick() {
            @Override
            public void Click(int position) {
                String url = bannerUrl[position];
                try {
                    String id = url.split("=")[1];
                    ((SupportFragment) getParentFragment()).start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_GOODS, id));
                } catch (Exception e) {
                }


            }
        });
        sv.setScaleType();

    }

    private void setCenter(List<HomeMallBean.Center> center) {
        mCenter = center;
        ImageView iv = (ImageView) header.findViewById(R.id.banner_mid);
        final String url = mCenter.get(0).getAd_link();
        Glide.with(getActivity()).load(Http.AD_PIC_URL + center.get(0).getCenter_url()).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = url.split("=")[1];

                ((SupportFragment) getParentFragment()).start(GoodsListFragment.newInstance(id, -1));
            }
        });

    }

    private void setHot(final List<HomeMallBean.Hot> hot) {
        mHot = hot;
        List<ImageView> ivList = new ArrayList<>();
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv1));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv2));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv3));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv4));
        int i = 0;
        for (ImageView iv : ivList) {
            final String url = hot.get(i).getAd_link();
            loadPic(Http.AD_PIC_URL + hot.get(i).getHot_url(), iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = url.split("=")[1];
                    ((SupportFragment) getParentFragment()).start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_GOODS, id));
                }
            });
            i++;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            String url = data.getStringExtra("result");

            if (!TextUtils.isEmpty(url)) {
                startActivity(new Intent(getActivity(), WebViewActivity_.class).putExtra(KV.URL, url));
            }
        }
    }

    private void onWeb(String url) {
        startActivity(new Intent(getActivity(), WebViewActivity_.class).putExtra(KV.URL, url));
    }

    StringCallback callback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {

        }

        @Override
        public void onResponse(String response, int id) {

            AdBean bean = Http.model(AdBean.class, response);
            if (bean.getCode().equals("200")) {
                adText.setText(bean.getData());

            }
        }
    };
}
