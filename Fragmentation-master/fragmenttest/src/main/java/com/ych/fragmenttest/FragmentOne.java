package com.ych.fragmenttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by ych on 2016/8/31.
 */
public class FragmentOne extends SupportFragment{

    public static FragmentOne getInstance(){
        Bundle bundle=new Bundle();
        FragmentOne one=new FragmentOne();
        one.setArguments(bundle);
        return one;
    }

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_one,null);
        initView();
        
        return view;
        
    }
Button button;
    private void initView() {
        button= (Button) view.findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.onBackPressed();
            }
        });

    }
}
