package com.ych.mall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ych.mall.R;
import com.ych.mall.event.MallAndTravelEvent;
import com.ych.mall.widget.SlideShowView;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ych on 2016/9/5.
 */
public class HomeTravelAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Context context;
    private String[] topImg;
    private String[] titles;

    //头部
    private void top(RecyclerView.ViewHolder holder, int position) {
        TOPHolder h1 = (TOPHolder) holder;
        h1.mShowView.setData(new String[]{topImg[0], topImg[1]});
        loadImg(topImg[2], h1.bannerMid);
        h1.toMall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EventBus.getDefault().post(new MallAndTravelEvent(MallAndTravelEvent.TYPE_CHANGE,0));

            }
        });
    }

    //中部
    private void mid(RecyclerView.ViewHolder holder, int position) {
        MIDHolder h3 = (MIDHolder) holder;
        loadImg(topImg[3], h3.banner1);
        loadImg(topImg[4], h3.banner2);
        loadImg(topImg[5], h3.banner3);
        loadImg(topImg[6], h3.banner4);


    }

    //商品列表
    private void list(RecyclerView.ViewHolder holder, int position) {
        LISTHolder h2 = (LISTHolder) holder;
        h2.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MallAndTravelEvent(MallAndTravelEvent.TYPE_TRVEL, 1));
            }
        });
        loadImg(titles[position], h2.img);
    }

    public enum ITEM_TYPE {
        ITEM_TOP,
        ITEM_MID,
        ITEM_LIST
    }

    public HomeTravelAdapter(Context context, String[] titles, String[] topImg) {
        this.titles = titles;
        this.context = context;
        this.topImg = topImg;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM_TOP.ordinal()) {
            return new TOPHolder(mLayoutInflater.inflate(R.layout.item_home_travel_top, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM_MID.ordinal()) {
            return new MIDHolder(mLayoutInflater.inflate(R.layout.item_home_travel_mid, parent, false));
        } else {
            return new LISTHolder(mLayoutInflater.inflate(R.layout.item_home_travel_goods, parent, false));
        }


    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TOPHolder) {
            top(holder, position);
        } else if (holder instanceof MIDHolder) {
            mid(holder, position);
        } else {
            list(holder, position);
        }

    }

    //设置ITEM类型，可以自由发挥，这里设置item position单数显示item1 偶数显示item2
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE.ITEM_TOP.ordinal();
        } else if (position == 1)
            return ITEM_TYPE.ITEM_MID.ordinal();
        else
            return ITEM_TYPE.ITEM_LIST.ordinal();
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.length;
    }

    //item1 的ViewHolder
    public static class TOPHolder extends RecyclerView.ViewHolder {
        SlideShowView mShowView;
        TextView toMall;
        ImageView bannerMid;

        public TOPHolder(View itemView) {
            super(itemView);
            mShowView = (SlideShowView) itemView.findViewById(R.id.banner);
            toMall = (TextView) itemView.findViewById(R.id.toMall);
            bannerMid = (ImageView) itemView.findViewById(R.id.banner_mid);

        }
    }

    public static class MIDHolder extends RecyclerView.ViewHolder {
        ImageView banner1, banner2, banner3, banner4;

        public MIDHolder(View itemView) {
            super(itemView);
            banner1 = (ImageView) itemView.findViewById(R.id.banner_bottom_iv1);
            banner2 = (ImageView) itemView.findViewById(R.id.banner_bottom_iv2);
            banner3 = (ImageView) itemView.findViewById(R.id.banner_bottom_iv3);
            banner4 = (ImageView) itemView.findViewById(R.id.banner_bottom_iv4);
        }
    }

    //item2 的ViewHolder
    public static class LISTHolder extends RecyclerView.ViewHolder {
        ImageView img;

        public LISTHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.it_img);

        }

    }

    private void loadImg(String url, ImageView iv) {
        Glide.with(context).load(url).into(iv);
    }

}
