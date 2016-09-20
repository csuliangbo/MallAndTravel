package com.ych.mall.ui.first.child.childpager;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.ych.mall.R;
import com.ych.mall.ui.base.BaseFragment;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


/**
 * Created by ych on 2016/9/5.
 */
@EFragment(R.layout.fragment_comment)
public class CommentFragment extends BaseFragment {
    @ViewById(R.id.fc_rv)
    RecyclerView tv;
    public static CommentFragment newInstance(){
        Bundle bundle=new Bundle();
        CommentFragment fragment=new CommentFragment_();
        fragment.setArguments(bundle);
        return fragment;
    }
}
