package com.ych.fragmenttest;

import android.graphics.Color;
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
public class FragmentHome extends SupportFragment {

    public static FragmentHome getInstance() {
        Bundle bundle = new Bundle();
        FragmentHome home = new FragmentHome();
        home.setArguments(bundle);
        return home;
    }

    ProgressButton button;
    View view;
    Button load, stop, err,change;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        initView();
        return view;
    }


    private void initView() {

        button = (ProgressButton) view.findViewById(R.id.btn1);
        button.setText("你好", "加載中...", "加载错误");
        button.setTextColor(getResources().getColor(R.color.white));
        button.setPBClickListener(new ProgressButton.PBClickListener() {
            @Override
            public void onClick() {

            }
        });

        load = (Button) view.findViewById(R.id.load);
        stop = (Button) view.findViewById(R.id.stop);
        err = (Button) view.findViewById(R.id.err);
        change = (Button)view.findViewById(R.id.change);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.startLoading();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.stop();
            }
        });
        err.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.err();
            }
        });
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setErrDrawale(getResources().getDrawable(R.drawable.shape_red_5dp));
            }
        });

    }
}
