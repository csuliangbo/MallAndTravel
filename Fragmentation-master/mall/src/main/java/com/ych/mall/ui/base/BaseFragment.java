package com.ych.mall.ui.base;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by YoKeyword on 16/2/3.
 */
public class BaseFragment extends SupportFragment {
    private static final String TAG = "Fragmentation";

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
        tv.setText(str);
    }

    public void hideSoftKeyBord() {
        View view = getActivity().getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
