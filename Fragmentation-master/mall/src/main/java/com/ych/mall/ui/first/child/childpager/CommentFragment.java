package com.ych.mall.ui.first.child.childpager;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;
import com.ych.mall.utils.KV;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * Created by ych on 2016/9/5.
 */
@EFragment(R.layout.fragment_comment)
public class CommentFragment extends BaseFragment {
    @ViewById(R.id.fc_rv)
    RecyclerView tv;
    int mType;
    String mId;
    public static CommentFragment newInstance(int type, String id){
        Bundle bundle=new Bundle();
        bundle.putInt(KV.TYPE, type);
        bundle.putString(KV.ID, id);
        CommentFragment fragment=new CommentFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }
    @AfterViews
    void init(){
        mType=getArguments().getInt(KV.TYPE);
        mId=getArguments().getString(KV.ID);

    }
}
