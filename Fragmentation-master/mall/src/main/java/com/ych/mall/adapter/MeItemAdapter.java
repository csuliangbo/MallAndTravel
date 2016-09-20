package com.ych.mall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.MeItemBean;
import com.ych.mall.widget.ClearEditText;

import java.util.List;

/**
 * Created by ych on 2016/9/9.
 */
public class MeItemAdapter extends BaseAdapter {
    Context mContext;
    List<MeItemBean> mDatas;
    public MeItemAdapter(Context context,List<MeItemBean> datas){
        mContext=context;
        mDatas=datas;
    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView==null)
        {
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.item_me,null);
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.img= (ImageView) convertView.findViewById(R.id.img);
            convertView.setTag(holder);

        }else
        holder= (ViewHolder) convertView.getTag();
        holder.name.setText(mDatas.get(position).getName());
        holder.img.setImageResource(mDatas.get(position).getImg());
        return convertView;
    }

    class ViewHolder{
        TextView name;
        ImageView img;
    }
}
