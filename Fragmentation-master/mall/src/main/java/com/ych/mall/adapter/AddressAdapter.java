package com.ych.mall.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ych.mall.R;
import com.ych.mall.bean.AddressBean;

import java.util.List;

/**
 * Created by ych on 2016/9/7.
 */
public class AddressAdapter extends BaseAdapter{

    private List<AddressBean> mDatas;
    private Context mContext;

    public AddressAdapter(List<AddressBean> datas,Context context){
        mDatas=datas;
        mContext=context;
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
        if(convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.item_address,null);
            holder.name= (TextView) convertView.findViewById(R.id.name);
            holder.address= (TextView) convertView.findViewById(R.id.add);
            holder.defaultAdd= (TextView) convertView.findViewById(R.id.defalut);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    class ViewHolder{
        TextView name,address,defaultAdd;
    }
}
