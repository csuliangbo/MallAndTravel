package com.ych.mall.ui.third.child;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.ParentBean;
import com.ych.mall.bean.ShopCarBean;
import com.ych.mall.bean.ShopCarBean.ShopCarData;
import com.ych.mall.model.Http;
import com.ych.mall.model.MallAndTravelModel;
import com.ych.mall.model.RecyclerViewModel;
import com.ych.mall.model.RecyclerViewNormalModel;
import com.ych.mall.model.YViewHolder;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.Tools;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by ych on 2016/9/8.
 */
@EFragment(R.layout.fragment_shop_car)
public class ShopCarFragment extends BaseFragment implements RecyclerViewModel.RModelListener<ShopCarData> {
    public static ShopCarFragment newInstance() {
        Bundle bundle = new Bundle();
        ShopCarFragment fragment = new ShopCarFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }

    @ViewById
    ImageView onBack;
    @ViewById
    TextView tiTitle;
    @ViewById
    TextView tiText;
    @ViewById
    FrameLayout mLoading;
    @ViewById
    TextView mPrice;

    @Click
    void onSubmit() {

    }

    RecyclerViewModel<ShopCarData> model;
    @ViewById
    RecyclerView mList;
    @ViewById
    SwipeRefreshLayout mLayout;
    List<String> idList;
@Click
void tiText(){

}
    @AfterViews
    public void initViews() {
        onBack.setVisibility(View.GONE);
        tiText.setVisibility(View.VISIBLE);
        tiText.setText("清空");
        tiTitle.setText("购物车");
        idList = new ArrayList<>();
        model = new RecyclerViewModel<>(getActivity(), this, mList, mLayout,
                R.layout.item_shop_car);
        model.init();

    }

    @Override
    public void getData(StringCallback callback, int page) {
        MallAndTravelModel.shopCarList(callback, page);
    }

    @Override
    public void onErr(int state) {

        TOT("网络连接失败");
        mLayout.setVisibility(View.GONE);
    }

    @Override
    public List<ShopCarData> getList(String str) {

        mLoading.setVisibility(View.GONE);
        ShopCarBean bean = Http.model(ShopCarBean.class, str);
        if (bean.getCode().equals("200")) {
            return bean.getData();
        }
        return null;
    }

    @Override
    public void covert(YViewHolder holder, ShopCarData t) {
        final ShopCarData d = t;
        final int num = Integer.parseInt(t.getGoods_num());
        final double pri = Double.parseDouble(t.getPrice_new());
        final double totleP = Tools.mul(num, pri);
        holder.setText(R.id.name, t.getTitle());
        holder.setText(R.id.price, "￥" + t.getPrice_new());
        holder.setText(R.id.priceAll, totleP + "");
        holder.setText(R.id.integral, "返利：" + t.getFanli_jifen() + "积分");
        holder.setText(R.id.num, num + "");
        holder.loadImg(getActivity(), R.id.pic, Http.GOODS_PIC_URL + t.getPic_url());
        final CheckBox cb = holder.getView(R.id.select);
        final String id = t.getCart_id();
        if (t.isSelect()) {
            cb.setChecked(true);
        } else
            cb.setChecked(false);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb.isChecked()) {
                    idList.add(id);
                    d.setSelect(true);

                } else {
                    idList.remove(id);
                    d.setSelect(false);

                }
                getTotle();
            }
        });

        holder.getView(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int n = num + 1;
                d.setGoods_num(n + "");
                model.noti();
                getTotle();
            }
        });
        holder.getView(R.id.cut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int n = num - 1;
                if (n == 0)
                    n = 1;
                else
                    d.setGoods_num(n + "");
                model.noti();
                getTotle();
            }
        });
        holder.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MallAndTravelModel.shopCarDel(delCallback, id);
                mLoading.setVisibility(View.VISIBLE);
            }
        });
    }

    void getTotle() {
        double p = 0d;
        for (ShopCarData t : model.dataList) {
            if (!t.isSelect())
                continue;
            int num = Integer.parseInt(t.getGoods_num());
            double pri = Double.parseDouble(t.getPrice_new());
            double totleP = Tools.mul(num, pri);
            p = Tools.add(p, totleP);
        }
        mPrice.setText("应付：￥" + p + "元");
    }

    //删除
    StringCallback delCallback = new StringCallback() {
        @Override
        public void onError(Call call, Exception e, int id) {
            TOT("删除失败");
            mLoading.setVisibility(View.GONE);
        }

        @Override
        public void onResponse(String response, int id) {
            mLoading.setVisibility(View.GONE);

            ParentBean bean = Http.model(ParentBean.class, response);
            if (bean.getCode().equals("200")) {
                TOT("删除成功");
                model.onRefresh();
                idList.clear();
            }
        }
    };
}
