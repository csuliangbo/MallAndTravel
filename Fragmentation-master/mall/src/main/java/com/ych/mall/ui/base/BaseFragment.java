package com.ych.mall.ui.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ych.mall.MyApp;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseFragment extends SupportFragment {

    public void back() {
        _mActivity.onBackPressed();
    }

    public boolean isEmp(TextView v, String warm) {
        if (v.getText().toString() == null || v.getText().toString().trim().equals("")) {
            TOT(warm);
            return true;
        } else
            return false;
    }

    public String getT(TextView tv) {
        return tv.getText().toString().trim();
    }

    public void TOT(String str) {
        try {
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }
    }

    public void sT(TextView tv, String str) {
        if (str != null)
            tv.setText(str);
    }

    public void hideSoftKeyBord() {
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void loadPic(String url, ImageView iv) {
        Glide.with(this).load(url).into(iv);
    }

    private String TAG;

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public void log(String text) {
        l(text);
    }

    public void log(int text) {
        l(text+"");
    }

    private void l(String text) {
        if (TAG == null)
            TAG = "ych";
        if (!MyApp.isRelease)
            Log.i(TAG, text);
    }
}
