package com.ych.mall.ui.base;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ych on 2016/9/12.
 */
public class BaseActivity extends Activity {
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
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void sT(TextView tv, String str) {
        if (str!=null)
            tv.setText(str);
    }

    public void hideSoftKeyBord(){
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
