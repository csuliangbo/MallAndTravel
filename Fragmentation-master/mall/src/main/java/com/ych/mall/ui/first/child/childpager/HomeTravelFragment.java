package com.ych.mall.ui.first.child.childpager;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.bumptech.glide.Glide;
import com.umeng.socialize.utils.Log;
import com.ych.mall.R;
import com.ych.mall.adapter.HomeMallAdapter;
import com.ych.mall.adapter.HomeTravelAdapter;
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
import com.ych.mall.widget.ClearEditText;
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
    @ViewById(R.id.refresh_layout)
    SwipeRefreshLayout rLayout;
    @ViewById(R.id.rv_list)
    RecyclerView list;
    @ViewById
    ClearEditText mSearch;
    HomeTravelBean mData;
    RecyclerViewModel<HomeTravelBean.Clas> model;
    RecyclerViewHeader header;
    List<HomeTravelBean.Bannner> mBanner;
    List<HomeTravelBean.Center> mCenter;
    List<HomeTravelBean.Hot> mHot;

    public static int REQUEST_CODE = 123;

    public static HomeTravelFragment newInstance() {
        Bundle bundle = new Bundle();
        HomeTravelFragment fragment = new HomeTravelFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }


    @AfterViews
    public void ininList() {

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
                rLayout,
                R.layout.item_home_travel_goods);

        model.initWithHead(header);
    }


    @Click
    void onLogin() {
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
        //跳转到扫一扫
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
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
            if (mBanner == null || mCenter == null || mHot == null) {
                setBanner(bean.getData().getBannner());
                setCenter(bean.getData().getCenter());
                setHot(bean.getData().getHot());
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
                ((SupportFragment) getParentFragment()).start(TravelListFragment.newInstance(id));
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
                String url=bannerUrl[position];
                String id = url.split("=")[1];
                ((SupportFragment) getParentFragment()).start(GoodsViewPagerFragment.newInstance(GoodsFragment.TYPE_TRAVEL, id));
            }
        });

    }

    private void setCenter(List<HomeTravelBean.Center> center) {
        mCenter = center;
        ImageView iv = (ImageView) header.findViewById(R.id.banner_mid);
        final String url = mCenter.get(0).getAd_link();
        Glide.with(getActivity()).load(Http.AD_PIC_URL + center.get(0).getFooter_url()).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWeb(url);
            }
        });

    }

    private void setHot(final List<HomeTravelBean.Hot> hot) {
        mHot = hot;
        List<ImageView> ivList = new ArrayList<>();
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv1));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv2));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv3));
        ivList.add((ImageView) header.findViewById(R.id.banner_bottom_iv4));
        int i = 0;
        for (ImageView iv : ivList) {
            final String id = hot.get(i).getId();
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
