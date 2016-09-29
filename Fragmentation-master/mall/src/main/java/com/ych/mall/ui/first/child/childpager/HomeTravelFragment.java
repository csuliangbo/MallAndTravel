package com.ych.mall.ui.first.child.childpager;

import android.Manifest;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.umeng.socialize.utils.Log;
import com.ych.mall.R;
import com.ych.mall.adapter.HomeMallAdapter;
import com.ych.mall.adapter.HomeTravelAdapter;
import com.ych.mall.bean.HomeMallBean;
import com.ych.mall.bean.HomeTravelBean;
import com.ych.mall.event.MainEvent;
import com.ych.mall.event.MallAndTravelEvent;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.LoginActivity_;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.ui.first.child.GoodsViewPagerFragment;
import com.ych.mall.ui.first.child.TravelListFragment;
import com.ych.mall.ui.fourth.WebViewActivity_;
import com.ych.mall.utils.KV;
import com.ych.mall.utils.UserCenter;
import com.ych.mall.widget.ClearEditText;
import com.ych.mall.widget.MyScrollView;
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

/**
 * Created by ych on 2016/8/31.
 */
@EFragment(R.layout.fragment_home_travel)
public class HomeTravelFragment extends BaseFragment implements RecyclerViewModel.RModelListener<HomeTravelBean.Clas> {
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

    LinearLayout mItem1, mItem2, mItem3, mItem4, mItem5;

    HomeTravelBean mData;
    RecyclerViewModel<HomeTravelBean.Clas> model;
    RecyclerViewHeader header;
    List<HomeTravelBean.Bannner> mBanner;
    List<HomeTravelBean.Center> mCenter;
    List<HomeTravelBean.Hot> mHot;
    List<HomeTravelBean.Class_page> page;
    public final static int REQUEST_CODE = 123;

    public static HomeTravelFragment newInstance() {
        Bundle bundle = new Bundle();
        HomeTravelFragment fragment = new HomeTravelFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }


    @AfterViews
    public void ininList() {
setTAG("hometravel");
        header = RecyclerViewHeader.fromXml(getActivity(), R.layout.head_travel);
        header.findViewById(R.id.toMall).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MallAndTravelEvent(MallAndTravelEvent.TYPE_CHANGE, 0));
            }
        });
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
        switch (requestCode){
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

        ((SupportFragment) getParentFragment()).start(SearchTravelFragment.newInstance(mSearch.getText().toString(), GoodsFragment.TYPE_TRAVEL));

    }


    @Override
    public void getData(StringCallback callback, int page) {
        MallAndTravelModel.homeTravel(callback, page);
    }

    @Override
    public void onErr(int state) {
        TOT("网络连接失败");
    }


    @Override
    public List<HomeTravelBean.Clas> getList(String str) {

        HomeTravelBean bean = Http.model(HomeTravelBean.class, str);
        if (bean.getCode().equals("200")) {
            if (mBanner == null) {
                setBanner(bean.getData().getBannner());
                setCenter(bean.getData().getCenter());
                try {
                    setHot(bean.getData().getHot());
                }catch (Exception e)
                {

                }
                setPage(bean.getData().getClass_page());
            }
            return bean.getData().getClas();

        }
        return null;
    }


    @Override
    public void covert(YViewHolder holder, HomeTravelBean.Clas t) {
        final String id = t.getClass_id();
        holder.loadImg(getActivity(), R.id.it_img, Http.CLASS_PIC_URL + t.getClass_url());
        holder.getCovertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SupportFragment) getParentFragment()).start(TravelListFragment.newInstance(id,0));
            }
        });
    }

    String[] bannerImg;
    String[] bannerUrl;
    SlideShowView sv;

    private void setBanner(List<HomeTravelBean.Bannner> banner) {
        mBanner = banner;
        sv = (SlideShowView) header.findViewById(R.id.banner);
        final TextView toTravel = (TextView) header.findViewById(R.id.toTravel);
        ImageView bannerMid = (ImageView) header.findViewById(R.id.banner_mid);
        bannerImg = new String[mBanner.size()];
        bannerUrl = new String[mBanner.size()];
        int i = 0;
        for (HomeTravelBean.Bannner b : mBanner) {
            bannerImg[i] = Http.AD_PIC_URL + b.getBannner_url();
            bannerUrl[i] = b.getAd_link();
            i++;
        }
        sv.setData(bannerImg);

        sv.setListener(new SlideShowView.OnVClick() {
            @Override
            public void Click(int position) {
                String url = bannerUrl[position];
                String id = url.split("=")[1];
                ((SupportFragment) getParentFragment()).start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_TRAVEL, id));
            }
        });
        sv.setScaleType();

    }

    private void setCenter(List<HomeTravelBean.Center> center) {
        mCenter = center;
        ImageView iv = (ImageView) header.findViewById(R.id.banner_mid);
        final String url = mCenter.get(0).getAd_link();
        Glide.with(getActivity()).load(Http.AD_PIC_URL + center.get(0).getFooter_url()).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onWeb(url);
            }
        });

    }

    private void setHot(final List<HomeTravelBean.Hot> hot) {
        mHot = hot;
        log(hot.toString());
        List<ImageView> ivList = new ArrayList<>();
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv1));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv2));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv3));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv4));
        int i = 0;
        for (ImageView iv : ivList) {
            final String id = hot.get(i).getAd_link().split("=")[1];
            loadPic(Http.AD_PIC_URL + hot.get(i).getSige_url(), iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ((SupportFragment) getParentFragment()).start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_TRAVEL, id));

                }
            });
            i++;
        }
    }

    List<LinearLayout> vList;

    private void setPage(List<HomeTravelBean.Class_page> page) {
        vList = new ArrayList<>();
        mItem1 = (LinearLayout) header.findViewById(R.id.mItem1);
        mItem2 = (LinearLayout) header.findViewById(R.id.mItem2);
        mItem3 = (LinearLayout) header.findViewById(R.id.mItem3);
        mItem4 = (LinearLayout) header.findViewById(R.id.mItem4);
        mItem5 = (LinearLayout) header.findViewById(R.id.mItem5);
        vList.add(mItem1);
        vList.add(mItem2);
        vList.add(mItem3);
        vList.add(mItem4);
        vList.add(mItem5);
        int i = 0;
        setTAG("travel");
        log(page.size());

        for (HomeTravelBean.Class_page p : page) {
            final String id = p.getClass_id();
            vList.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SupportFragment) getParentFragment()).start(TravelListFragment.newInstance(id,-1));
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
}
